package com.cjhub.data.repositories

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Single
import io.reactivex.observers.TestObserver

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

import com.cjhub.data.dao.CategoryDaoImpl
import com.cjhub.data.entities.CategoryEntity

/**
 * Spek tests for Category repository.
 */
class CategoryRepositoryFeature : Spek({

    Feature("Get Categories") {

        val categoryDao by memoized { mock<CategoryDaoImpl>() }
        val categoryMapper by memoized { mock<Mapper<CategoryEntity, Category>>() }

        val categoryRepository by memoized { CategoryRepositoryImpl(categoryDao, categoryMapper) }

        val firstCategoryEntity = CategoryEntity(
            1L,
            "Salary",
            "Income",
            1000.0f,
            System.currentTimeMillis()
        )
        val secondCategoryEntity = CategoryEntity(
            1L,
            "Food",
            "Expense",
            500.0f,
            System.currentTimeMillis()
        )
        val thirdCategoryEntity = CategoryEntity(
            3L,
            "Bonus",
            "Income",
            500.0f,
            System.currentTimeMillis()
        )

        val firstCategoryModel = Category(1L, "Salary", Type.INCOME, 1000.0f)
        val secondCategoryModel = Category(2L, "Food", Type.EXPENSE, 500.0f)
        val thirdCategoryModel = Category(3L, "Bonus", Type.INCOME, 500.0f)

        lateinit var categoryEntities: List<CategoryEntity>

        lateinit var testObserver: TestObserver<List<Category>>

        Scenario("The system wants to get the list of categories") {

            Given("a list of categories exists") {
                categoryEntities = listOf(firstCategoryEntity, secondCategoryEntity, thirdCategoryEntity)
            }
            When("the system gets the list of categories") {
                whenever(categoryDao.getAll()).thenReturn(Single.just(categoryEntities))
                whenever(categoryMapper.toModel(firstCategoryEntity)).thenReturn(firstCategoryModel)
                whenever(categoryMapper.toModel(secondCategoryEntity)).thenReturn(secondCategoryModel)
                whenever(categoryMapper.toModel(thirdCategoryEntity)).thenReturn(thirdCategoryModel)

                testObserver = categoryRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                categoryEntities.forEach { categoryEntity ->
                    verify(categoryMapper).toModel(categoryEntity)
                }
            }
            And("the list of categories should be returned") {
                testObserver.assertResult(listOf(
                    firstCategoryModel, secondCategoryModel, thirdCategoryModel
                ))
            }
        }

        Scenario("The system wants to get the list of income categories") {

            Given("a list of income categories exists") {
                categoryEntities = listOf(firstCategoryEntity, thirdCategoryEntity)
            }
            When("the system gets the list of categories") {
                whenever(categoryDao.getAll()).thenReturn(Single.just(categoryEntities))
                whenever(categoryMapper.toModel(firstCategoryEntity)).thenReturn(firstCategoryModel)
                whenever(categoryMapper.toModel(thirdCategoryEntity)).thenReturn(thirdCategoryModel)

                testObserver = categoryRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                categoryEntities.forEach { categoryEntity ->
                    verify(categoryMapper).toModel(categoryEntity)
                }
            }
            And("the list of categories should be returned") {
                testObserver.assertResult(listOf(firstCategoryModel, thirdCategoryModel))
            }
        }
    }

    Feature("Insert or Update Category") {

        val categoryDao by memoized { mock<CategoryDaoImpl>() }
        val categoryMapper by memoized { mock<Mapper<CategoryEntity, Category>>() }

        val categoryRepository by memoized { CategoryRepositoryImpl(categoryDao, categoryMapper) }

        Scenario("The system wants to insert or update a category") {

            val categoryEntity = CategoryEntity(
                1L,
                "Salary",
                "Income",
                1000.0f,
                System.currentTimeMillis()
            )

            lateinit var categoryModel: Category

            lateinit var testObserver: TestObserver<Void>

            Given("a category to be inserted or updated") {
                categoryModel = Category(1L, "Salary", Type.INCOME, 1000.0f)
            }
            When("the system inserts or updates the category") {
                whenever(categoryMapper.toEntity(categoryModel)).thenReturn(categoryEntity)

                testObserver = categoryRepository.insertOrUpdate(categoryModel).test()
            }
            Then("the system should insert or update successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<CategoryEntity> {
                    verify(categoryDao).insertOrUpdate(capture())
                    assert(firstValue == categoryEntity)
                }
            }
        }
    }

    Feature("Delete Category") {

        val categoryDao by memoized { mock<CategoryDaoImpl>() }
        val categoryMapper by memoized { mock<Mapper<CategoryEntity, Category>>() }

        val categoryRepository by memoized { CategoryRepositoryImpl(categoryDao, categoryMapper) }

        Scenario("The system wants to delete a category") {

            val categoryEntity = CategoryEntity(
                1L,
                "Salary",
                "Income",
                1000.0f,
                System.currentTimeMillis()
            )

            lateinit var categoryModel: Category

            lateinit var testObserver: TestObserver<Void>

            Given("a category to be deleted") {
                categoryModel = Category(1L, "Salary", Type.INCOME, 1000.0f)
            }
            When("the system deletes the category") {
                whenever(categoryMapper.toEntity(categoryModel)).thenReturn(categoryEntity)

                testObserver = categoryRepository.delete(categoryModel).test()
            }
            Then("the system should delete successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<CategoryEntity> {
                    verify(categoryDao).delete(capture())
                    assert(firstValue == categoryEntity)
                }
            }
        }
    }

    Feature("Reset Categories") {

        val categoryDao by memoized { mock<CategoryDaoImpl>() }
        val categoryMapper by memoized { mock<Mapper<CategoryEntity, Category>>() }

        val categoryRepository by memoized { CategoryRepositoryImpl(categoryDao, categoryMapper) }

        Scenario("The system wants to reset all categories") {

            lateinit var testObserver: TestObserver<Void>

            When("the system resets all categories") {
                testObserver = categoryRepository.reset().test()
            }
            Then("the system should reset successfully") {
                testObserver.assertComplete()
            }
        }
    }
})
