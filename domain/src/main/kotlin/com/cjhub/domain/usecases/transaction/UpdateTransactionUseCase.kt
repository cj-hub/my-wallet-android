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
 * Update an existing transaction and store it in the database.
 */
class UpdateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun update(oldTransaction: Transaction, newTransaction: Transaction): Completable {
        return if (!newTransaction.isValidForUpdate(oldTransaction)) {
            Completable.error(IllegalArgumentException(
                "Transaction update exceeds the current balance"
            ))
        } else {
            val updatedOldCategory = updateOldCategory(oldTransaction)
            val updatedOldSourceAccount = updateOldSourceAccount(oldTransaction)
            val updatedOldDestinationAccount = updateOldDestinationAccount(oldTransaction)
            val updatedNewCategory = updateNewCategory(oldTransaction, newTransaction)
            val updatedNewSourceAccount = updateNewSourceAccount(oldTransaction, newTransaction)
            val updatedNewDestinationAccount = updateNewDestinationAccount(oldTransaction, newTransaction)

            transactionRepository.insertOrUpdate(newTransaction.copy(
                category = updatedNewCategory,
                sourceAccount = updatedNewSourceAccount,
                destinationAccount = updatedNewDestinationAccount
            )).andThen(categoryRepository.insertOrUpdate(updatedNewCategory).andThen(
                if (oldTransaction.category == newTransaction.category) {
                    Completable.complete()
                } else {
                    categoryRepository.insertOrUpdate(updatedOldCategory)
                }
            )).andThen(accountRepository.insertOrUpdate(updatedNewSourceAccount).andThen(
                if (oldTransaction.sourceAccount == newTransaction.sourceAccount) {
                    Completable.complete()
                } else {
                    accountRepository.insertOrUpdate(updatedOldSourceAccount)
                }
            )).andThen(when (oldTransaction.destinationAccount) {
                Account.NO_ACCOUNT -> Completable.complete()
                else -> {
                    accountRepository.insertOrUpdate(updatedNewDestinationAccount).andThen(
                        if (oldTransaction.destinationAccount == newTransaction.destinationAccount) {
                            Completable.complete()
                        } else {
                            accountRepository.insertOrUpdate(updatedOldDestinationAccount)
                        }
                    )
                }
            })
        }
    }

    private fun updateOldCategory(transaction: Transaction): Category {
        return transaction.category.copy(
            total = transaction.category.total - transaction.amount
        )
    }

    private fun updateOldSourceAccount(transaction: Transaction): Account {
        return transaction.sourceAccount.copy(
            balance = transaction.sourceAccount.balance
                    - computeAmount(transaction)
        )
    }

    private fun updateOldDestinationAccount(transaction: Transaction): Account {
        return when (transaction.destinationAccount) {
            Account.NO_ACCOUNT -> Account.NO_ACCOUNT
            else -> {
                transaction.destinationAccount.copy(
                    balance = transaction.destinationAccount.balance - transaction.amount
                )
            }
        }
    }

    private fun updateNewCategory(oldTransaction: Transaction, newTransaction: Transaction): Category {
        return newTransaction.category.copy(
            total = if (oldTransaction.category == newTransaction.category) {
                oldTransaction.category.total - oldTransaction.amount
            } else {
                newTransaction.category.total
            } + newTransaction.amount
        )
    }

    private fun updateNewSourceAccount(oldTransaction: Transaction, newTransaction: Transaction): Account {
        return newTransaction.sourceAccount.copy(
            balance = if (oldTransaction.sourceAccount == newTransaction.sourceAccount) {
                oldTransaction.sourceAccount.balance - computeAmount(oldTransaction)
            } else {
                newTransaction.sourceAccount.balance
            } + computeAmount(newTransaction)
        )
    }

    private fun updateNewDestinationAccount(oldTransaction: Transaction, newTransaction: Transaction): Account {
        return when (newTransaction.destinationAccount) {
            Account.NO_ACCOUNT -> Account.NO_ACCOUNT
            else -> {
                newTransaction.destinationAccount.copy(
                    balance = if (oldTransaction.destinationAccount == newTransaction.destinationAccount) {
                        oldTransaction.destinationAccount.balance - oldTransaction.amount
                    } else {
                        newTransaction.destinationAccount.balance
                    } + newTransaction.amount
                )
            }
        }
    }

    private fun computeAmount(transaction: Transaction): Float {
         return when (transaction.category.type) {
             Type.INCOME -> transaction.amount
             Type.EXPENSE, Type.TRANSFER -> -transaction.amount
             else -> 0.0f
         }
    }
}
