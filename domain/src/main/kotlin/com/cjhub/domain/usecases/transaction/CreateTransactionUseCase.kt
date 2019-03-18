package com.cjhub.domain.usecases.transaction

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

/**
 * Create a new transaction and store it in the database.
 */
class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun create(transaction: Transaction): Completable {
        if (!transaction.isValidForCreate()) {
            return Completable.error(IllegalArgumentException(
                "New transaction exceeds the current balance"
            ))
        } else {
            val category = transaction.category
            val sourceAccount = transaction.sourceAccount
            val destinationAccount = transaction.destinationAccount

            val completable = transactionRepository.insertOrUpdate(transaction)
                    .andThen(categoryRepository.insertOrUpdate(
                        category.copy(total = category.total + transaction.amount)
                    ))
                    .andThen(accountRepository.insertOrUpdate(
                        sourceAccount.copy(balance = sourceAccount.balance + when (category.type) {
                            Type.INCOME -> transaction.amount
                            Type.EXPENSE, Type.TRANSFER -> - transaction.amount
                            else -> 0.0f
                        })
                    ))
            return if (category.type == Type.TRANSFER) {
                completable.andThen(accountRepository.insertOrUpdate(destinationAccount.copy(
                    balance = destinationAccount.balance + transaction.amount
                )))
            } else {
                completable
            }
        }
    }
}
