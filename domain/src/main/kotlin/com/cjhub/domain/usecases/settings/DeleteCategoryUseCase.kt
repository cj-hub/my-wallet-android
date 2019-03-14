package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Category

/**
 * Delete an existing category from the database.
 */
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) {

    fun delete(category: Category): Completable = TODO()
}