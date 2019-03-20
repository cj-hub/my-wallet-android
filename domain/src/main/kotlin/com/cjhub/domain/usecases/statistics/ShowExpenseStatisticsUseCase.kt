package com.cjhub.domain.usecases.statistics

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.usecases.utilities.ShowExpenseCategoriesUseCase

/**
 * Show expense statistics of the transactions in the database.
 */
class ShowExpenseStatisticsUseCase(private val categoryRepository: CategoryRepository) {

    fun show(): Single<List<Category>> = ShowExpenseCategoriesUseCase(categoryRepository).show()
}
