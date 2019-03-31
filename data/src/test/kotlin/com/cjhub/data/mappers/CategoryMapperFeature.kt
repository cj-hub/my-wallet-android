package com.cjhub.data.mappers

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

import com.cjhub.data.entities.CategoryEntity

/**
 * Spek tests for Category mapper.
 */
class CategoryMapperFeature : Spek({

    Feature("Category Entity Mapper") {

        val categoryMapper by memoized { CategoryMapperImpl() }

        Scenario("The system wants to map a category model to a corresponding entity") {

            lateinit var categoryModel: Category

            lateinit var categoryEntity: CategoryEntity

            Given("a category entity") {
                categoryModel = Category(1L, "Salary", Type.INCOME, 1000.0f)
            }
            When("the system maps to a category entity") {
                categoryEntity = categoryMapper.toEntity(categoryModel)
            }
            Then("a valid category entity should be returned") {
                assert(categoryEntity.id == 1L)
                assert(categoryEntity.name == "Salary")
                assert(categoryEntity.type == Type.INCOME.toString())
                assert(categoryEntity.total == 1000.0f)
                assert(categoryEntity.timestamp > 0L)
            }
        }
    }

    Feature("Category Model Mapper") {

        val categoryMapper by memoized { CategoryMapperImpl() }

        Scenario("The system wants to map a category entity to a corresponding model") {

            lateinit var categoryEntity: CategoryEntity

            lateinit var categoryModel: Category

            Given("a category entity") {
                categoryEntity = CategoryEntity(1L, "Salary", "Income", 1000.0f, System.currentTimeMillis())
            }
            When("the system maps to a category model") {
                categoryModel = categoryMapper.toModel(categoryEntity)
            }
            Then("a valid category model should be returned") {
                assert(categoryModel.id == 1L)
                assert(categoryModel.name == "Salary")
                assert(categoryModel.type == Type.INCOME)
                assert(categoryModel.total == 1000.0f)
            }
        }
    }
})
