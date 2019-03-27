package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Category

import com.cjhub.data.entities.CategoryEntity

/**
 * Mapper between Category entity and domain model.
 */
class CategoryMapperImpl : Mapper<CategoryEntity, Category> {

    override fun toModel(from: CategoryEntity): Category = TODO()

    override fun toEntity(from: Category): CategoryEntity = TODO()
}
