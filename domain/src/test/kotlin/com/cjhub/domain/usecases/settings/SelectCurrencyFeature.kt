package com.cjhub.domain.usecases.settings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.CurrencyRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Currency

/**
 * Spek tests for currency selection use case.
 */
object SelectCurrencyFeature : Spek({

    Feature("Select Currency") {

        val currencyRepository by memoized { mock<CurrencyRepository>() }
        val transactionRepository by memoized { mock<TransactionRepository>() }
        val categoryRepository by memoized { mock<CategoryRepository>() }
        val accountRepository by memoized { mock<AccountRepository>() }

        val selectCurrencyUseCase by memoized { SelectCurrencyUseCase(
                currencyRepository, transactionRepository, categoryRepository, accountRepository
        )}

        lateinit var newCurrency: Currency

        Scenario("The user wants to select a new currency") {

            val currency = Currency(1L, "USD", "$", true)

            val updatedCurrency = Currency(1L, "USD", "$", false)

            Given("a new currency") {
                newCurrency = Currency(2L, "THB", "฿", true)
            }
            When("the user selects a new currency") {
                whenever(currencyRepository.update(updatedCurrency)).thenReturn(Completable.complete())
                whenever(currencyRepository.update(newCurrency)).thenReturn(Completable.complete())
                whenever(transactionRepository.clear()).thenReturn(Completable.complete())
                whenever(categoryRepository.reset()).thenReturn(Completable.complete())
                whenever(accountRepository.reset()).thenReturn(Completable.complete())

                selectCurrencyUseCase.select(currency, newCurrency)
            }
            Then("the system should update the currency selection") {
                verify(currencyRepository).update(updatedCurrency)
                verify(currencyRepository).update(newCurrency)
            }
            And("the system should clear all transactions") {
                verify(transactionRepository).clear()
            }
            And("the system should reset all categories' totals") {
                verify(categoryRepository).reset()
            }
            And("the system should reset all accounts' balances") {
                verify(accountRepository).reset()
            }
        }
    }
})
