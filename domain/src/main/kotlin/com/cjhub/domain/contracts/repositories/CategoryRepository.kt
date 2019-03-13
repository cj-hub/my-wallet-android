package com.cjhub.domain.contracts.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

/**
 * Category repository interface for data layer.
 */
interface CategoryRepository {

    fun getAll(): Single<List<Category>>

    fun getByType(type: Type): Single<List<Category>>

    fun insertOrUpdate(category: Category): Completable

    fun delete(category: Category): Completable
}
