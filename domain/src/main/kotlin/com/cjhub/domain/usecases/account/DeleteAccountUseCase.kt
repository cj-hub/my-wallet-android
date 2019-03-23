package com.cjhub.domain.usecases.account

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

/**
 * Delete an existing account from the database.
 */
class DeleteAccountUseCase(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
) {

    fun delete(account: Account, relatedTransactions: List<Transaction>): Completable {
        return if (!account.isValidForDelete(relatedTransactions)) {
            Completable.error(IllegalArgumentException(
                "Account deletion results in negative balances for other accounts"
            ))
        } else {
            val updatedCategories = relatedTransactions.groupBy({ it.category }, { it.amount })
                    .map { (category, amounts) ->
                        category.copy(total = category.total - amounts.sum())
                    }
            val updatedAccounts = relatedTransactions.filter { transaction ->
                        transaction.category.type == Type.TRANSFER
                    }
                    .map { transaction ->
                        if (transaction.sourceAccount == account) {
                            AccountAmountPair(transaction.destinationAccount, -transaction.amount)
                        } else {
                            AccountAmountPair(transaction.sourceAccount, transaction.amount)
                        }
                    }
                    .groupBy({ it.account }, { it.amount })
                    .map { (account, amounts) ->
                        account.copy(balance = account.balance + amounts.sum())
                    }

            accountRepository.delete(account)
                    .andThen(Completable.merge(
                        updatedCategories.map { updatedCategory ->
                            categoryRepository.insertOrUpdate(updatedCategory)
                        }
                    ))
                    .andThen(Completable.merge(
                        updatedAccounts.map { updatedAccount ->
                            accountRepository.insertOrUpdate(updatedAccount)
                        }
                    ))
        }
    }

    data class AccountAmountPair(val account: Account, val amount: Float)
}
