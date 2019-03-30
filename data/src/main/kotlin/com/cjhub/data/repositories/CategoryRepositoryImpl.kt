package com.cjhub.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

import com.cjhub.data.dao.CategoryDaoImpl
import com.cjhub.data.entities.CategoryEntity

/**
 * Room implementation of Category repository.
 */
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDaoImpl,
    private val categoryMapper: Mapper<CategoryEntity, Category>
) : CategoryRepository {

    override fun getAll(): Single<List<Category>> {
        return categoryDao.getAll().map { categories -> categories.map(categoryMapper::toModel) }
    }

    override fun getAllByType(type: Type): Single<List<Category>> {
        return categoryDao.getAllByType(type.toString()).map { categories ->
            categories.map(categoryMapper::toModel)
        }
    }

    override fun insertOrUpdate(category: Category): Completable {
        return Completable.fromAction { categoryDao.insertOrUpdate(categoryMapper.toEntity(category)) }
    }

    override fun delete(category: Category): Completable {
        return Completable.fromAction { categoryDao.delete(categoryMapper.toEntity(category)) }
    }

    override fun reset(): Completable {
        return Completable.fromAction { categoryDao.reset() }
    }
}
