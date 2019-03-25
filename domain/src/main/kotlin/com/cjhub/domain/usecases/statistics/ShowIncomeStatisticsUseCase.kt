package com.cjhub.domain.usecases.statistics

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.usecases.utilities.ShowIncomeCategoriesUseCase

/**
 * Show income statistics of the transactions in the database.
 */
class ShowIncomeStatisticsUseCase(private val categoryRepository: CategoryRepository) {

    fun show(): Single<List<Category>> = ShowIncomeCategoriesUseCase(categoryRepository).show()
}
