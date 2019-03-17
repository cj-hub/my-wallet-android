package com.cjhub.domain.usecases.utilities

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

/**
 * Show all the income categories stored in the database.
 */
class ShowIncomeCategoriesUseCase(private val categoryRepository: CategoryRepository) {

    fun show(): Single<List<Category>> = categoryRepository.getAllByType(Type.INCOME)
}
