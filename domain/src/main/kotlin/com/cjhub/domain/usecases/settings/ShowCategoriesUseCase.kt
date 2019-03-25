package com.cjhub.domain.usecases.settings

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category

/**
 * Show all the categories stored in the database.
 */
class ShowCategoriesUseCase(private val categoryRepository: CategoryRepository) {

    fun show(): Single<List<Category>> = categoryRepository.getAll()
}
