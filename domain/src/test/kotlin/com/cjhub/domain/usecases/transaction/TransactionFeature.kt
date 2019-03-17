package com.cjhub.domain.usecases.transaction

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

import java.util.GregorianCalendar

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

    Feature("Create Transaction") {

        val transactionRepository by memoized { mock<TransactionRepository>() }
        val categoryRepository by memoized { mock<CategoryRepository>() }
        val accountRepository by memoized { mock<AccountRepository>() }

        val createTransactionUseCase by memoized {
            CreateTransactionUseCase(transactionRepository, categoryRepository, accountRepository)
        }

        lateinit var newTransaction: Transaction

        Scenario("The user wants to create a valid income transaction") {

            Given("a valid income transaction") {
                newTransaction = Transaction(
                    1L,
                    GregorianCalendar.getInstance().time,
                    Account(1L, "My Wallet", 0.0f),
                    Category(1L, "Salary", Type.INCOME, 0.0f),
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
            }
            When("the user creates a new income transaction") {
                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should insert the new income transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(
                    Category(
                        1L, "Salary", Type.INCOME, 1000.0f
                    )
                )
            }
            And("the system should update the specified account's balance") {
                verify(accountRepository).insertOrUpdate(Account(
                    1L, "My Wallet", 1000.0f
                ))
            }
        }

        Scenario("The user wants to create a valid expense transaction") {

            Given("a valid expense transaction") {
                newTransaction = Transaction(
                    1L,
                    GregorianCalendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Food", Type.EXPENSE, 0.0f),
                    Account.NO_ACCOUNT,
                    900.0f,
                    "Today's groceries"
                )
            }
            When("the user creates a new expense transaction") {
                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should insert the new expense transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(Category(
                    1L, "Food", Type.EXPENSE, 900.0f
                ))
            }
            And("the system should update the specified account's balance") {
                verify(accountRepository).insertOrUpdate(Account(
                    1L, "My Wallet", 100.0f
                ))
            }
        }

        Scenario("The user wants to create an invalid expense transaction") {

            Given("an invalid expense transaction") {
                newTransaction = Transaction(
                    1L,
                    GregorianCalendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Food", Type.EXPENSE, 0.0f),
                    Account.NO_ACCOUNT,
                    1200.0f,
                    "Today's groceries"
                )
            }
            When("the user creates a new expense transaction") {
                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should not insert the new expense transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update the specified category's total") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update the specified account's balance") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to create a valid transfer transaction") {

            Given("a valid transfer transaction") {
                newTransaction = Transaction(
                    1L,
                    GregorianCalendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Transfer", Type.TRANSFER, 0.0f),
                    Account(2L, "Bank", 0.0f),
                    1000.0f,
                    "This month's savings"
                )
            }
            When("the user creates a new transfer transaction") {
                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should insert the new transfer transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(Category(
                    1L, "Transfer", Type.TRANSFER, 1000.0f
                ))
            }
            And("the system should update the original account's balance") {
                verify(accountRepository).insertOrUpdate(Account(
                    1L, "My Wallet", 0.0f
                ))
            }
            And("the system should update the new account's balance") {
                verify(accountRepository).insertOrUpdate(Account(
                    2L, "Bank", 1000.0f
                ))
            }
        }

        Scenario("The user wants to create an invalid transfer transaction") {

            Given("an invalid transfer transaction") {
                newTransaction = Transaction(
                    1L,
                    GregorianCalendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Transfer", Type.TRANSFER, 0.0f),
                    Account(2L, "Bank", 0.0f),
                    1500.0f,
                    "This month's savings"
                )
            }
            When("the user creates a new transfer transaction") {
                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should not insert the new transfer transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update the specified category's total") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update any of the two accounts' balances") {
                verifyZeroInteractions(accountRepository)
            }
        }
    }
})
