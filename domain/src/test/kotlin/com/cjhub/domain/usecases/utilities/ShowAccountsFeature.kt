package com.cjhub.domain.usecases.utilities

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.AccountRepository

/**
 * Spek tests for showing existing accounts.
 */
object ShowAccountsFeature : Spek({

    Feature("Show Accounts") {

        val accountRepository by memoized { mock<AccountRepository>() }

        val showAccountsUseCase by memoized { ShowAccountsUseCase(accountRepository) }

        Scenario("The user wants to request for the accounts") {

            When("the user requests for the accounts") {
                showAccountsUseCase.show()
            }
            Then("the system should get the list of accounts") {
                verify(accountRepository).getAll()
            }
        }
    }
})
