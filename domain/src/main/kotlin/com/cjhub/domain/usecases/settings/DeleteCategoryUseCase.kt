package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction

/**
 * Delete an existing category from the database.
 */
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) {

    fun delete(
        category: Category,
        relatedTransactions: List<Transaction>,
        otherIncome: Category,
        otherExpense: Category
    ): Completable = TODO()
}
