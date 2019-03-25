package com.cjhub.domain.usecases.utilities

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify

import com.cjhub.domain.contracts.repositories.CurrencyRepository

/**
 * Spek tests for showing available currencies.
 */
object ShowCurrenciesFeature : Spek({

    Feature("Show Currencies") {

        val currencyRepository by memoized { mock<CurrencyRepository>() }

        val showCurrenciesUseCase by memoized { ShowCurrenciesUseCase(currencyRepository) }

        Scenario("The user wants to request for the currencies") {

            When("the user requests for the currencies") {
                showCurrenciesUseCase.show()
            }
            Then("the system should get the list of currencies") {
                verify(currencyRepository).getAll()
            }
        }
    }
})
