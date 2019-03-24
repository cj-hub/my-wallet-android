package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction

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
    ): Completable = TODO()
}
