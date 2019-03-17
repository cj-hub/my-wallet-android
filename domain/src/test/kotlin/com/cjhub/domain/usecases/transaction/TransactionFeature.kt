package com.cjhub.domain.usecases.transaction

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock

import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.nhaarman.mockitokotlin2.verify

/**
 * Spek tests for various transaction use cases.
 */
object TransactionFeature : Spek({

    Feature("Show Transactions") {

        val transactionRepository by memoized { mock<TransactionRepository>() }

        val showTransactionsUseCase by memoized { ShowTransactionsUseCase(transactionRepository) }

        Scenario("The user wants to request for the transactions") {

            When("the user requests for the transactions") {
                showTransactionsUseCase.show()
            }
            Then("the system should get the list of transactions") {
                verify(transactionRepository).getAll()
            }
        }
    }
})
