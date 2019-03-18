package com.cjhub.domain.usecases.transaction

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

import java.util.Calendar

/**
 * Spek tests for various transaction use cases.
 */
object TransactionFeatures : Spek({

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

            val newCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val newSourceAccount = Account(1L, "My Wallet", 1000.0f)

            Given("a valid income transaction") {
                newTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    Account(1L, "My Wallet", 0.0f),
                    Category(1L, "Salary", Type.INCOME, 0.0f),
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
            }
            When("the user creates a new income transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction).subscribe()
            }
            Then("the system should insert the new income transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the specified account's balance") {
                verify(accountRepository).insertOrUpdate(newSourceAccount)
            }
        }

        Scenario("The user wants to create a valid expense transaction") {

            val newCategory = Category(1L, "Food", Type.EXPENSE, 900.0f)
            val newSourceAccount = Account(1L, "My Wallet", 100.0f)

            Given("a valid expense transaction") {
                newTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Food", Type.EXPENSE, 0.0f),
                    Account.NO_ACCOUNT,
                    900.0f,
                    "Today's groceries"
                )
            }
            When("the user creates a new expense transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction).subscribe()
            }
            Then("the system should insert the new expense transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(newSourceAccount)
            }
        }

        Scenario("The user wants to create an invalid expense transaction") {

            val newCategory = Category(1L, "Food", Type.EXPENSE, 1200.0f)
            val newSourceAccount = Account(1L, "My Wallet", -200.0f)

            Given("an invalid expense transaction") {
                newTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Food", Type.EXPENSE, 0.0f),
                    Account.NO_ACCOUNT,
                    1200.0f,
                    "Today's groceries"
                )
            }
            When("the user creates a new expense transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction).subscribe({}, {})
            }
            Then("the system should not insert the new expense transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update the specified category's total") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update the specified source account's balance") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to create a valid transfer transaction") {

            val newCategory = Category(1L, "Transfer", Type.TRANSFER, 1000.0f)
            val newSourceAccount = Account(1L, "My Wallet", 0.0f)
            val newDestinationAccount = Account(2L, "Bank", 1000.0f)

            Given("a valid transfer transaction") {
                newTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Transfer", Type.TRANSFER, 0.0f),
                    Account(2L, "Bank", 0.0f),
                    1000.0f,
                    "This month's savings"
                )
            }
            When("the user creates a new transfer transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newDestinationAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction).subscribe()
            }
            Then("the system should insert the new transfer transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the source account's balance") {
                verify(accountRepository).insertOrUpdate(newSourceAccount)
            }
            And("the system should update the destination account's balance") {
                verify(accountRepository).insertOrUpdate(newDestinationAccount)
            }
        }

        Scenario("The user wants to create an invalid transfer transaction") {

            val newCategory = Category(1L, "Transfer", Type.TRANSFER, 1500.0f)
            val newSourceAccount = Account(1L, "My Wallet", -500.0f)
            val newDestinationAccount = Account(2L, "Bank", 1500.0f)

            Given("an invalid transfer transaction") {
                newTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Transfer", Type.TRANSFER, 0.0f),
                    Account(2L, "Bank", 0.0f),
                    1500.0f,
                    "This month's savings"
                )
            }
            When("the user creates a new transfer transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newDestinationAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction).subscribe({}, {})
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

    Feature("Update Transaction") {

        val transactionRepository by memoized { mock<TransactionRepository>() }
        val categoryRepository by memoized { mock<CategoryRepository>() }
        val accountRepository by memoized { mock<AccountRepository>() }

        val updateTransactionUseCase by memoized {
            UpdateTransactionUseCase(transactionRepository, categoryRepository, accountRepository)
        }

        lateinit var oldTransaction: Transaction
        lateinit var newTransaction: Transaction

        Scenario("The user wants to update a valid transaction") {

            val oldCategory = Category(1L, "Salary", Type.INCOME, 500.0f)
            val oldSourceAccount = Account(1L, "My Wallet", 500.0f)

            val newCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val newSourceAccount = Account(1L, "My Wallet", 1000.0f)

            Given("a valid transaction update") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's salary"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    oldSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
            }
            When("the user updates the transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(newSourceAccount)
            }
        }

        Scenario("The user wants to update an invalid transaction") {

            val oldCategory = Category(1L, "Food", Type.EXPENSE, 500.0f)
            val oldSourceAccount = Account(1L, "My Wallet", 0.0f)

            val newCategory = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            val newSourceAccount = Account(1L, "My Wallet", -500.0f)

            Given("an invalid transaction update") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "Today's groceries"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    oldSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "Today's groceries"
                )
            }
            When("the user updates the transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should not update the transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update the specified category's total") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update the specified source account's balance") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to change the category with enough balance") {

            val oldFirstCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val oldSecondCategory = Category(2L, "Bonus", Type.INCOME, 0.0f)
            val oldSourceAccount = Account(1L, "My Wallet", 1000.0f)

            val newFirstCategory = Category(1L, "Salary", Type.INCOME, 0.0f)
            val newSecondCategory = Category(2L, "Bonus", Type.INCOME, 500.0f)
            val newSourceAccount = Account(1L, "My Wallet", 500.0f)

            Given("a change in category for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldSourceAccount,
                    oldFirstCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    oldSourceAccount,
                    oldSecondCategory,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's bonus"
                )
            }
            When("the user changes the category") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newFirstCategory))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newSecondCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified old category's total") {
                verify(categoryRepository).insertOrUpdate(newFirstCategory)
            }
            And("the system should update the specified new category's total") {
                verify(categoryRepository).insertOrUpdate(newSecondCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(newSourceAccount)
            }
        }

        Scenario("The user wants to change the category without enough balance") {

            val oldFirstCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val oldSecondCategory = Category(2L, "Food", Type.EXPENSE, 0.0f)
            val oldSourceAccount = Account(1L, "My Wallet", 1000.0f)

            val newFirstCategory = Category(1L, "Salary", Type.INCOME, 0.0f)
            val newSecondCategory = Category(2L, "Food", Type.EXPENSE, 500.0f)
            val newSourceAccount = Account(1L, "My Wallet", -500.0f)

            Given("a change in category for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldSourceAccount,
                    oldFirstCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    oldSourceAccount,
                    oldSecondCategory,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "Today's groceries"
                )
            }
            When("the user changes the category") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newFirstCategory))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newSecondCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should not update the transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update both of the categories' totals") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update the specified source account's balance") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to change the account with enough balance") {

            val oldCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val oldFirstSourceAccount = Account(1L, "My Wallet", 1000.0f)
            val oldSecondSourceAccount = Account(2L, "Bank", 0.0f)

            val newCategory = Category(1L, "Salary", Type.INCOME, 500.0f)
            val newFirstSourceAccount = Account(1L, "My Wallet", 0.0f)
            val newSecondSourceAccount = Account(2L, "Bank", 500.0f)

            Given("a change in account for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldFirstSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    oldSecondSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's salary"
                )
            }
            When("the user changes the account") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newFirstSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSecondSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the specified old source account's balance") {
                verify(accountRepository).insertOrUpdate(newFirstSourceAccount)
            }
            And("the system should update the specified new source account's balance") {
                verify(accountRepository).insertOrUpdate(newSecondSourceAccount)
            }
        }

        Scenario("The user wants to change the account without enough balance") {

            val oldCategory = Category(1L, "Food", Type.EXPENSE, 500.0f)
            val oldFirstSourceAccount = Account(1L, "My Wallet", 500.0f)
            val oldSecondSourceAccount = Account(2L, "Bank", 0.0f)

            val newCategory = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            val newFirstSourceAccount = Account(1L, "My Wallet", 1000.0f)
            val newSecondSourceAccount = Account(2L, "Bank", -1000.0f)

            Given("a change in account for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldFirstSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "Today's groceries"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    oldSecondSourceAccount,
                    oldCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "Today's groceries"
                )
            }
            When("the user changes the account") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newFirstSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSecondSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should not update the transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update the specified category's total") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update both of the source accounts' balances") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to make a transfer with enough balance") {

            val oldCategory = Category(1L, "Transfer", Type.TRANSFER, 500.0f)
            val oldSourceAccount = Account(1L, "My Wallet", 500.0f)
            val oldDestinationAccount = Account(2L, "Bank", 500.0f)

            val newCategory = Category(1L, "Transfer", Type.TRANSFER, 1000.0f)
            val newSourceAccount = Account(1L, "My Wallet", 0.0f)
            val newDestinationAccount = Account(2L, "Bank", 1000.0f)

            Given("a transfer transaction") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldSourceAccount,
                    oldCategory,
                    oldDestinationAccount,
                    500.0f,
                    "This month's savings"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    newSourceAccount,
                    newCategory,
                    newDestinationAccount,
                    1000.0f,
                    "This month's savings"
                )
            }
            When("the user makes a transfer") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newDestinationAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the source account's balance") {
                verify(accountRepository).insertOrUpdate(newSourceAccount)
            }
            And("the system should update the destination account's balance") {
                verify(accountRepository).insertOrUpdate(newDestinationAccount)
            }
        }

        Scenario("The user wants to make a transfer without enough balance") {

            val oldCategory = Category(1L, "Transfer", Type.TRANSFER, 500.0f)
            val oldSourceAccount = Account(1L, "My Wallet", 500.0f)
            val oldDestinationAccount = Account(2L, "Bank", 500.0f)

            val newCategory = Category(1L, "Transfer", Type.TRANSFER, 1500.0f)
            val newSourceAccount = Account(1L, "My Wallet", -500.0f)
            val newDestinationAccount = Account(2L, "Bank", 1500.0f)

            Given("a transfer transaction") {
                oldTransaction = Transaction(
                    1L,
                    Calendar.getInstance().time,
                    oldSourceAccount,
                    oldCategory,
                    oldDestinationAccount,
                    500.0f,
                    "This month's savings"
                )
                newTransaction = Transaction(
                    oldTransaction.id,
                    oldTransaction.dateTime,
                    newSourceAccount,
                    newCategory,
                    newDestinationAccount,
                    1500.0f,
                    "This month's savings"
                )
            }
            When("the user makes a transfer") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(newDestinationAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should not update the transaction") {
                verifyZeroInteractions(transactionRepository)
            }
            And("the system should not update the specified category's total") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update both of the accounts' balances") {
                verifyZeroInteractions(accountRepository)
            }
        }
    }
})
