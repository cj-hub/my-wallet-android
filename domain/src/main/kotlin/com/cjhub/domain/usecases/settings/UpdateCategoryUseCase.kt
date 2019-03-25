package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

/**
 * Update an existing category and store it in the database.
 */
class UpdateCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun update(
        oldCategory: Category,
        newCategory: Category,
        relatedTransactions: List<Transaction>
    ): Completable {
        return if (!oldCategory.isValidForUpdate(newCategory, relatedTransactions)) {
            Completable.error(IllegalArgumentException(
                "Category's type change results in negative balances for existing accounts"
            ))
        } else {
            val updatedAccounts = relatedTransactions.groupBy({ it.sourceAccount }, { it.amount })
                    .map { (account, amounts) ->
                        if (oldCategory.type == newCategory.type) {
                            account
                        } else {
                            when (oldCategory.type) {
                                Type.INCOME -> {
                                    account.copy(balance = account.balance - 2 * amounts.sum())
                                }
                                else -> {
                                    account.copy(balance = account.balance + 2 * amounts.sum())
                                }
                            }
                        }
                    }

            categoryRepository.insertOrUpdate(newCategory)
                    .andThen(if (oldCategory.type == newCategory.type) {
                        Completable.complete()
                    } else {
                        Completable.merge(updatedAccounts.map { updatedAccount ->
                            accountRepository.insertOrUpdate(updatedAccount)
                        })
                    })
        }
    }
}
