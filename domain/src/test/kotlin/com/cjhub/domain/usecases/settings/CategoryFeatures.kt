package com.cjhub.domain.usecases.settings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.CategoryRepository

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
})
