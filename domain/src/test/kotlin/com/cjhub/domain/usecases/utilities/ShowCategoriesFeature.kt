package com.cjhub.domain.usecases.utilities

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Type

/**
 * Spek tests for showing different types of categories.
 */
object ShowCategoriesFeature : Spek({

    Feature("Show Categories") {

        val categoryRepository by memoized { mock<CategoryRepository>() }

        val showIncomeCategoriesUseCase by memoized {
            ShowIncomeCategoriesUseCase(categoryRepository)
        }
        val showExpenseCategoriesUseCase by memoized {
            ShowExpenseCategoriesUseCase(categoryRepository)
        }

        Scenario("The user wants to request for the income categories") {

            When("the user requests for the income categories") {
                showIncomeCategoriesUseCase.show()
            }
            Then("the system should get the list of income categories") {
                verify(categoryRepository).getAllByType(Type.INCOME)
            }
        }

        Scenario("The user wants to request for the expense categories") {

            When("the user requests for the expense categories") {
                showExpenseCategoriesUseCase.show()
            }
            Then("the system should get the list of expense categories") {
                verify(categoryRepository).getAllByType(Type.EXPENSE)
            }
        }
    }
})
