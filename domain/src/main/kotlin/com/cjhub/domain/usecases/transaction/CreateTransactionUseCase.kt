package com.cjhub.domain.usecases.transaction

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
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
            val amountMultiplier = when (category.type) {
                Type.INCOME -> 1
                Type.EXPENSE, Type.TRANSFER -> -1
                else -> 0
            }

            val completable = transactionRepository.insertOrUpdate(transaction)
                .andThen(categoryRepository.insertOrUpdate(Category(
                    category.id,
                    category.name,
                    category.type,
                    category.total + transaction.amount
                )))
                .andThen(accountRepository.insertOrUpdate(Account(
                    sourceAccount.id,
                    sourceAccount.name,
                    sourceAccount.balance + transaction.amount * amountMultiplier
                )))
            return if (category.type == Type.TRANSFER) {
                completable.andThen(accountRepository.insertOrUpdate(Account(
                    destinationAccount.id,
                    destinationAccount.name,
                    destinationAccount.balance + transaction.amount
                )))
            } else {
                completable
            }
        }
    }
}
