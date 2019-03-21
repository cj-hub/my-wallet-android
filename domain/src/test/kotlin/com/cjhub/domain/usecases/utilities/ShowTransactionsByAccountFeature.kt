package com.cjhub.domain.usecases.utilities

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account

/**
 * Spek tests for showing transactions corresponding to an account
 */
object ShowTransactionsByAccountFeature : Spek({

    Feature("Show Transactions by Account") {

        val transactionRepository by memoized { mock<TransactionRepository>() }

        val showTransactionsByAccountUseCase by memoized {
            ShowTransactionsByAccountUseCase(transactionRepository)
        }

        lateinit var account: Account

        Scenario("The user wants to request for the transactions by an account") {

            Given("an account") {
                account = Account(1L, "My Wallet", 0.0f)
            }
            When("the user request for the transactions") {
                showTransactionsByAccountUseCase.showBy(account)
            }
            Then("the system should get the list of transactions corresponding to the account") {
                verify(transactionRepository).getAllByAccount(account)
            }
        }
    }
})
