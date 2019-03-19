package com.cjhub.domain.usecases.transaction

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

/**
 * Delete an existing transaction from the database.
 */
class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun delete(transaction: Transaction): Completable {
        val category = transaction.category
        val sourceAccount = transaction.sourceAccount

        return transactionRepository.delete(transaction)
                .andThen(categoryRepository.insertOrUpdate(category.copy(
                    total = category.total - transaction.amount
                )))
                .andThen(accountRepository.insertOrUpdate(sourceAccount.copy(
                    balance = sourceAccount.balance - computeAmount(transaction)
                )))
                .andThen(if (category.type == Type.TRANSFER) {
                    val destinationAccount = transaction.destinationAccount

                    accountRepository.insertOrUpdate(destinationAccount.copy(
                        balance = destinationAccount.balance - transaction.amount
                    ))
                } else {
                    Completable.complete()
                })
    }

    private fun computeAmount(transaction: Transaction): Float {
        return when (transaction.category.type) {
            Type.INCOME -> transaction.amount
            Type.EXPENSE, Type.TRANSFER -> -transaction.amount
            else -> 0.0f
        }
    }
}
