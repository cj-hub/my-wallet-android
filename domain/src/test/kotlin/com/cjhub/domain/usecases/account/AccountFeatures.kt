package com.cjhub.domain.usecases.account

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.models.Account

/**
 * Spek tests for various account use cases.
 */
object AccountFeatures : Spek({

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

    Feature("Create Account") {

        val accountRepository by memoized { mock<AccountRepository>() }

        val createAccountUseCase by memoized { CreateAccountUseCase(accountRepository) }

        lateinit var newAccount: Account

        Scenario("The user wants to create a new account") {

            Given("a new account") {
                newAccount = Account(1L, "My Wallet", 0.0f)
            }
            When("the user creates a new account") {
                whenever(accountRepository.insertOrUpdate(newAccount))
                        .thenReturn(Completable.complete())

                createAccountUseCase.create(newAccount)
            }
            Then("the system should insert the new account") {
                verify(accountRepository).insertOrUpdate(newAccount)
            }
        }
    }

    Feature("Update Account") {

        val accountRepository by memoized { mock<AccountRepository>() }

        val updateAccountUseCase by memoized { UpdateAccountUseCase(accountRepository) }

        lateinit var newAccount: Account

        Scenario("The user wants to update an account") {

            Given("an updated account") {
                newAccount = Account(1L, "Mon Portefeuille", 0.0f)
            }
            When("the user updates an account") {
                whenever(accountRepository.insertOrUpdate(newAccount))
                        .thenReturn(Completable.complete())

                updateAccountUseCase.update(newAccount)
            }
            Then("the system should update the account") {
                verify(accountRepository).insertOrUpdate(newAccount)
            }
        }
    }
})
