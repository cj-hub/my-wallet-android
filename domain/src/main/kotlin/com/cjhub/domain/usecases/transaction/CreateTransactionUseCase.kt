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

    fun create(transaction: Transaction): Completable = Completable.fromAction {
        val category = transaction.category
        val sourceAccount = transaction.sourceAccount

        if (category.type != Type.INCOME && transaction.amount > sourceAccount.balance) {
            return@fromAction
        }
        val amountMultiplier = when (category.type) {
            Type.INCOME -> 1
            Type.EXPENSE, Type.TRANSFER -> -1
            else -> 0
        }
        transactionRepository.insertOrUpdate(transaction)
        categoryRepository.insertOrUpdate(Category(
            category.id, category.name, category.type, category.total + transaction.amount
        ))
        accountRepository.insertOrUpdate(Account(
            sourceAccount.id,
            sourceAccount.name,
            sourceAccount.balance + transaction.amount * amountMultiplier
        ))
        if (category.type == Type.TRANSFER) {
            val destinationAccount = transaction.destinationAccount

            accountRepository.insertOrUpdate(Account(
                destinationAccount.id,
                destinationAccount.name,
                destinationAccount.balance + transaction.amount
            ))
        }
    }
}
