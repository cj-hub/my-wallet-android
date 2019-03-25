package com.cjhub.domain.usecases.statistics

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Type

/**
 * Spek tests for various statistics use cases.
 */
object StatisticsFeatures : Spek({

    Feature("Show Statistics") {

        val categoryRepository by memoized { mock<CategoryRepository>() }

        val showIncomeStatisticsUseCase by memoized {
            ShowIncomeStatisticsUseCase(categoryRepository)
        }
        val showExpenseStatisticsUseCase by memoized {
            ShowExpenseStatisticsUseCase(categoryRepository)
        }

        Scenario("The user wants to request for the income statistics") {

            When("the user requests for the income statistics") {
                showIncomeStatisticsUseCase.show()
            }
            Then("the system should get the list of income statistics") {
                verify(categoryRepository).getAllByType(Type.INCOME)
            }
        }

        Scenario("The user wants to request for the expense statistics") {

            When("the user requests for the expense statistics") {
                showExpenseStatisticsUseCase.show()
            }
            Then("the system should get the list of expense statistics") {
                verify(categoryRepository).getAllByType(Type.EXPENSE)
            }
        }
    }
})
