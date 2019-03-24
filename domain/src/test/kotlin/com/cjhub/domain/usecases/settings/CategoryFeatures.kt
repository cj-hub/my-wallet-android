package com.cjhub.domain.usecases.settings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

/**
 * Spek tests for various category use cases.
 */
object CategoryFeatures : Spek({

    Feature("Show Categories") {

        val categoryRepository by memoized { mock<CategoryRepository>() }

        val showCategoriesUseCase by memoized { ShowCategoriesUseCase(categoryRepository) }

        Scenario("The user wants to request for the categories") {

            When("the user requests for the categories") {
                showCategoriesUseCase.show()
            }
            Then("the system should get the list of categories") {
                verify(categoryRepository).getAll()
            }
        }
    }

    Feature("Create Category") {

        val categoryRepository by memoized { mock<CategoryRepository>() }

        val createCategoryUseCase by memoized { CreateCategoryUseCase(categoryRepository) }

        lateinit var newCategory: Category

        Scenario("The user wants to create a new category") {

            Given("a new category") {
                newCategory = Category(1L, "Salary", Type.INCOME, 0.0f)
            }
            When("the user creates a new category") {
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())

                createCategoryUseCase.create(newCategory)
            }
            Then("the system should insert the new category") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
        }
    }
})
