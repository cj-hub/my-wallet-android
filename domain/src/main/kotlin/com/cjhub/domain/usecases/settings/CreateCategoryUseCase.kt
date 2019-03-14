package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category

/**
 * Create a new category and store it in the database.
 */
class CreateCategoryUseCase(private val categoryRepository: CategoryRepository) {

    fun create(category: Category): Completable = TODO()
}
