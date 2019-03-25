package com.cjhub.domain.usecases.utilities

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Type

/**
 * Spek tests for showing transactions corresponding to a category.
 */
object ShowTransactionsByCategoryFeature : Spek({

    Feature("Show Transactions by Category") {

        val transactionRepository by memoized { mock<TransactionRepository>() }

        val showTransactionsByCategoryUseCase by memoized {
            ShowTransactionsByCategoryUseCase(transactionRepository)
        }

        lateinit var category: Category

        Scenario("The user wants to request for the transactions by a category") {

            Given("a category") {
                category = Category(1L, "Salary", Type.INCOME, 1000.0f)
            }
            When("the user request for the transactions") {
                showTransactionsByCategoryUseCase.showBy(category)
            }
            Then("the system should get the list of transactions corresponding to the category") {
                verify(transactionRepository).getAllByCategory(category)
            }
        }
    }
})
