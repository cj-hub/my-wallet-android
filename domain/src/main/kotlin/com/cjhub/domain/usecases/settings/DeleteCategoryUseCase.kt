package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

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
    ): Completable {
        val updatedCategory = when (category.type) {
            Type.INCOME -> otherIncome.copy(total = otherIncome.total + category.total)
            else -> otherExpense.copy(total = otherExpense.total + category.total)
        }

        return categoryRepository.insertOrUpdate(updatedCategory)
                .andThen(Completable.merge(relatedTransactions.map { transaction ->
                    transactionRepository.insertOrUpdate(transaction.copy(category = updatedCategory))
                }))
                .andThen(categoryRepository.delete(category))
    }
}
