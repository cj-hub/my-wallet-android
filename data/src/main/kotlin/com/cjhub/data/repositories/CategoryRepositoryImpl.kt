package com.cjhub.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.contracts.dao.CategoryDao
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

import com.cjhub.data.entities.CategoryEntity

/**
 * Room implementation of Category repository.
 */
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val categoryMapper: Mapper<CategoryEntity, Category>
) : CategoryRepository {

    override fun getAll(): Single<List<Category>> = TODO()

    override fun getAllByType(type: Type): Single<List<Category>> = TODO()

    override fun insertOrUpdate(category: Category): Completable = TODO()

    override fun delete(category: Category): Completable = TODO()

    override fun reset(): Completable = TODO()
}
