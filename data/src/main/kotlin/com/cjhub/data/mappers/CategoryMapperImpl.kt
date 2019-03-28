package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

import com.cjhub.data.entities.CategoryEntity

/**
 * Mapper between Category entity and domain model.
 */
class CategoryMapperImpl : Mapper<CategoryEntity, Category> {

    override fun toModel(from: CategoryEntity): Category {
        return Category(
            from.id,
            from.name,
            when (from.type) {
                "Income" -> Type.INCOME
                "Expense" -> Type.EXPENSE
                "Transfer" -> Type.TRANSFER
                else -> Type.NONE
            },
            from.total
        )
    }

    override fun toEntity(from: Category): CategoryEntity {
        return CategoryEntity(
            from.id,
            from.name,
            from.type.toString(),
            from.total,
            System.currentTimeMillis()

        )
    }
}
