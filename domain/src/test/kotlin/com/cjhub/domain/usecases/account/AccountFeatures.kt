package com.cjhub.domain.usecases.account

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

import java.time.LocalDateTime

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

    Feature("Delete Account") {

        val accountRepository by memoized { mock<AccountRepository>() }
        val categoryRepository by memoized { mock<CategoryRepository>() }

        val deleteAccountUseCase by memoized {
            DeleteAccountUseCase(accountRepository, categoryRepository)
        }

        lateinit var account: Account
        lateinit var relatedTransactions: List<Transaction>

        Scenario("The user wants to delete an account") {

            val incomeCategory = Category(1L, "Salary", Type.INCOME, 10000.0f)
            val expenseCategory = Category(2L, "Food", Type.EXPENSE, 5000.0f)

            val updatedIncomeCategory = Category(1L, "Salary", Type.INCOME, 5000.0f)
            val updatedExpenseCategory = Category(2L, "Food", Type.EXPENSE, 2500.0f)

            Given("an account and a list of transactions corresponding to the account") {
                account = Account(1L, "My Wallet", 2500.0f)
                relatedTransactions = listOf(
                    Transaction(
                        1L,
                        LocalDateTime.now(),
                        account,
                        incomeCategory,
                        Account.NO_ACCOUNT,
                        3000.0f,
                        "This month's salary"
                    ),
                    Transaction(
                        2L,
                        LocalDateTime.now(),
                        account,
                        expenseCategory,
                        Account.NO_ACCOUNT,
                        500.0f,
                        "Today's breakfast"
                    ),
                    Transaction(
                        3L,
                        LocalDateTime.now(),
                        account,
                        incomeCategory,
                        Account.NO_ACCOUNT,
                        500.0f,
                        "Gifts"
                    ),
                    Transaction(
                        4L,
                        LocalDateTime.now(),
                        account,
                        incomeCategory,
                        Account.NO_ACCOUNT,
                        1500.0f,
                        "This month's bonus"
                    ),
                    Transaction(
                        5L,
                        LocalDateTime.now(),
                        account,
                        expenseCategory,
                        Account.NO_ACCOUNT,
                        2000.0f,
                        "Today's dinner"
                    )
                )
            }
            When("the user deletes an account") {
                whenever(accountRepository.delete(account))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedIncomeCategory))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedExpenseCategory))
                        .thenReturn(Completable.complete())

                deleteAccountUseCase.delete(account, relatedTransactions)
            }
            Then("the system should delete the account") {
                verify(accountRepository).delete(account)
            }
            And("the system should update all the corresponding categories") {
                verify(categoryRepository).insertOrUpdate(updatedIncomeCategory)
                verify(categoryRepository).insertOrUpdate(updatedExpenseCategory)
            }
        }

        Scenario("The user wants to delete a source account in transfer transactions with enough balance") {

            val transferCategory = Category(1L, "Transfer", Type.TRANSFER, 5000.0f)
            val firstDestinationAccount = Account(2L, "Bank", 2500.0f)
            val secondDestinationAccount = Account(3L, "Another Bank", 500.0f)

            val updatedTransferCategory = Category(1L, "Transfer", Type.TRANSFER, 2500.0f)
            val updatedFirstDestinationAccount = Account(2L, "Bank", 500.0f)
            val updatedSecondDestinationAccount = Account(3L, "Another Bank", 0.0f)

            Given("a source account and a list of transfer transactions") {
                account = Account(1L, "My Wallet", 0.0f)
                relatedTransactions = listOf(
                    Transaction(
                        1L,
                        LocalDateTime.now(),
                        account,
                        transferCategory,
                        firstDestinationAccount,
                        500.0f,
                        "First savings"
                    ),
                    Transaction(
                        2L,
                        LocalDateTime.now(),
                        account,
                        transferCategory,
                        secondDestinationAccount,
                        500.0f,
                        "Second savings"
                    ),
                    Transaction(
                        3L,
                        LocalDateTime.now(),
                        account,
                        transferCategory,
                        firstDestinationAccount,
                        1500.0f,
                        "Third savings"
                    )
                )
            }
            When("the users delete a source account") {
                whenever(accountRepository.delete(account))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedTransferCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedFirstDestinationAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSecondDestinationAccount))
                        .thenReturn(Completable.complete())

                deleteAccountUseCase.delete(account, relatedTransactions)
            }
            Then("the system should delete the source account") {
                verify(accountRepository).delete(account)
            }
            And("the system should update the specified category") {
                verify(categoryRepository).insertOrUpdate(updatedTransferCategory)
            }
            And("the system should update all the corresponding destination accounts") {
                verify(accountRepository).insertOrUpdate(updatedFirstDestinationAccount)
                verify(accountRepository).insertOrUpdate(updatedSecondDestinationAccount)
            }
        }

        Scenario("The user wants to delete a source account in transfer transactions without enough balance") {

            val transferCategory = Category(1L, "Transfer", Type.TRANSFER, 5000.0f)
            val firstDestinationAccount = Account(2L, "Bank", 2500.0f)
            val secondDestinationAccount = Account(3L, "Another Bank", 0.0f)

            Given("a source account and a list of transfer transactions") {
                account = Account(1L, "My Wallet", 0.0f)
                relatedTransactions = listOf(
                    Transaction(
                        1L,
                        LocalDateTime.now(),
                        account,
                        transferCategory,
                        firstDestinationAccount,
                        500.0f,
                        "First savings"
                    ),
                    Transaction(
                        2L,
                        LocalDateTime.now(),
                        account,
                        transferCategory,
                        secondDestinationAccount,
                        500.0f,
                        "Second savings"
                    ),
                    Transaction(
                        3L,
                        LocalDateTime.now(),
                        account,
                        transferCategory,
                        firstDestinationAccount,
                        1500.0f,
                        "Third savings"
                    )
                )
            }
            When("the users delete a source account") {
                deleteAccountUseCase.delete(account, relatedTransactions)
            }
            Then("the system should not delete any of the accounts") {
                verifyZeroInteractions(accountRepository)
            }
            And("the system should not update the specified category") {
                verifyZeroInteractions(categoryRepository)
            }
        }

        Scenario("The user wants to delete a destination account") {

            val transferCategory = Category(1L, "Transfer", Type.TRANSFER, 5000.0f)
            val firstSourceAccount = Account(2L, "Bank", 500.0f)
            val secondSourceAccount = Account(3L, "Another Bank", 0.0f)

            val updatedTransferCategory = Category(1L, "Transfer", Type.TRANSFER, 2500.0f)
            val updatedFirstSourceAccount = Account(2L, "Bank", 2500.0f)
            val updatedSecondSourceAccount = Account(3L, "Another Bank", 500.0f)

            Given("a destination account and a list of transfer transactions") {
                account = Account(1L, "My Wallet", 2500.0f)
                relatedTransactions = listOf(
                    Transaction(
                        1L,
                        LocalDateTime.now(),
                        firstSourceAccount,
                        transferCategory,
                        account,
                        500.0f,
                        "First withdrawal"
                    ),
                    Transaction(
                        2L,
                        LocalDateTime.now(),
                        secondSourceAccount,
                        transferCategory,
                        account,
                        500.0f,
                        "Second withdrawal"
                    ),
                    Transaction(
                        3L,
                        LocalDateTime.now(),
                        firstSourceAccount,
                        transferCategory,
                        account,
                        1500.0f,
                        "Third withdrawal"
                    )
                )
            }
            When("the user deletes a destination account") {
                whenever(accountRepository.delete(account))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedTransferCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedFirstSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSecondSourceAccount))
                        .thenReturn(Completable.complete())

                deleteAccountUseCase.delete(account, relatedTransactions)
            }
            Then("the system should delete the destination account") {
                verify(accountRepository).delete(account)
            }
            And("the system should updated the specified category") {
                verify(categoryRepository).insertOrUpdate(updatedTransferCategory)
            }
            And("the system should update all of the corresponding source accounts") {
                verify(accountRepository).insertOrUpdate(updatedFirstSourceAccount)
                verify(accountRepository).insertOrUpdate(updatedSecondSourceAccount)
            }
        }
    }
})
