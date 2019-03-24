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

import java.time.LocalDateTime

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

            val category = Category(1L, "Salary", Type.INCOME, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 0.0f)

            val updatedCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 1000.0f)

            Given("a valid income transaction") {
                newTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
            }
            When("the user creates a new income transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should insert the new income transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to create a valid expense transaction") {

            val category = Category(1L, "Food", Type.EXPENSE, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)

            val updatedCategory = Category(1L, "Food", Type.EXPENSE, 900.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 100.0f)

            Given("a valid expense transaction") {
                newTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    900.0f,
                    "Today's groceries"
                )
            }
            When("the user creates a new expense transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should insert the new expense transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to create an invalid expense transaction") {

            val category = Category(1L, "Food", Type.EXPENSE, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)

            Given("an invalid expense transaction") {
                newTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
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
            And("the system should not update the specified source account's balance") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to create a valid transfer transaction") {

            val category = Category(1L, "Transfer", Type.TRANSFER, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)
            val destinationAccount = Account(2L, "Bank", 0.0f)

            val updatedCategory = Category(1L, "Transfer", Type.TRANSFER, 1000.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 0.0f)
            val updatedDestinationAccount = Account(2L, "Bank", 1000.0f)

            Given("a valid transfer transaction") {
                newTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    destinationAccount,
                    1000.0f,
                    "This month's savings"
                )
            }
            When("the user creates a new transfer transaction") {
                whenever(transactionRepository.insertOrUpdate(newTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedDestinationAccount))
                        .thenReturn(Completable.complete())

                createTransactionUseCase.create(newTransaction)
            }
            Then("the system should insert the new transfer transaction") {
                verify(transactionRepository).insertOrUpdate(newTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
            And("the system should update the destination account's balance") {
                verify(accountRepository).insertOrUpdate(updatedDestinationAccount)
            }
        }

        Scenario("The user wants to create an invalid transfer transaction") {

            val category = Category(1L, "Transfer", Type.TRANSFER, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)
            val destinationAccount = Account(2L, "Bank", 0.0f)

            Given("an invalid transfer transaction") {
                newTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                   destinationAccount,
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

    Feature("Update Transaction") {

        val transactionRepository by memoized { mock<TransactionRepository>() }
        val categoryRepository by memoized { mock<CategoryRepository>() }
        val accountRepository by memoized { mock<AccountRepository>() }

        val updateTransactionUseCase by memoized {
            UpdateTransactionUseCase(transactionRepository, categoryRepository, accountRepository)
        }

        lateinit var oldTransaction: Transaction
        lateinit var newTransaction: Transaction
        lateinit var updatedTransaction: Transaction

        Scenario("The user wants to update a valid transaction") {

            val category = Category(1L, "Salary", Type.INCOME, 500.0f)
            val sourceAccount = Account(1L, "My Wallet", 500.0f)

            val updatedCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 1000.0f)

            Given("a valid transaction update") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's salary"
                )
                newTransaction = oldTransaction.copy(amount = 1000.0f)
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSourceAccount,
                    category = updatedCategory
                )
            }
            When("the user updates the transaction") {
                whenever(transactionRepository.insertOrUpdate(updatedTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(updatedTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to update an invalid transaction") {

            val category = Category(1L, "Food", Type.EXPENSE, 500.0f)
            val sourceAccount = Account(1L, "My Wallet", 0.0f)

            val updatedCategory = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", -500.0f)

            Given("an invalid transaction update") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "Today's groceries"
                )
                newTransaction = oldTransaction.copy(amount = 1000.0f)
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSourceAccount,
                    category = updatedCategory
                )
            }
            When("the user updates the transaction") {
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

            val firstCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val secondCategory = Category(2L, "Bonus", Type.INCOME, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)

            val updatedFirstCategory = Category(1L, "Salary", Type.INCOME, 0.0f)
            val updatedSecondCategory = Category(2L, "Bonus", Type.INCOME, 500.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 500.0f)

            Given("a change in category for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    firstCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
                newTransaction = oldTransaction.copy(
                    category = secondCategory,
                    amount = 500.0f,
                    description = "This month's bonus"
                )
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSourceAccount,
                    category = updatedSecondCategory
                )
            }
            When("the user changes the category") {
                whenever(transactionRepository.insertOrUpdate(updatedTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedFirstCategory))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedSecondCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(updatedTransaction)
            }
            And("the system should update the specified old category's total") {
                verify(categoryRepository).insertOrUpdate(updatedFirstCategory)
            }
            And("the system should update the specified new category's total") {
                verify(categoryRepository).insertOrUpdate(updatedSecondCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to change the category without enough balance") {

            val firstCategory = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val secondCategory = Category(2L, "Food", Type.EXPENSE, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)

            val updatedSecondCategory = Category(2L, "Food", Type.EXPENSE, 500.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", -500.0f)

            Given("a change in category for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    firstCategory,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
                newTransaction = oldTransaction.copy(
                    category = secondCategory,
                    amount = 500.0f,
                    description = "Today's groceries"
                )
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSourceAccount,
                    category = updatedSecondCategory
                )
            }
            When("the user changes the category") {
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

            val category = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val firstSourceAccount = Account(1L, "My Wallet", 1000.0f)
            val secondSourceAccount = Account(2L, "Bank", 0.0f)

            val updatedCategory = Category(1L, "Salary", Type.INCOME, 500.0f)
            val updatedFirstSourceAccount = Account(1L, "My Wallet", 0.0f)
            val updatedSecondSourceAccount = Account(2L, "Bank", 500.0f)

            Given("a change in account for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    firstSourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
                newTransaction = oldTransaction.copy(
                    sourceAccount = secondSourceAccount,
                    amount = 500.0f
                )
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSecondSourceAccount,
                    category = updatedCategory
                )
            }
            When("the user changes the account") {
                whenever(transactionRepository.insertOrUpdate(updatedTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedFirstSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSecondSourceAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(updatedTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified old source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedFirstSourceAccount)
            }
            And("the system should update the specified new source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSecondSourceAccount)
            }
        }

        Scenario("The user wants to change the account without enough balance") {

            val category = Category(1L, "Food", Type.EXPENSE, 500.0f)
            val firstSourceAccount = Account(1L, "My Wallet", 500.0f)
            val secondSourceAccount = Account(2L, "Bank", 0.0f)

            val updatedCategory = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            val updatedSecondSourceAccount = Account(2L, "Bank", -1000.0f)

            Given("a change in account for the transaction") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    firstSourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "Today's groceries"
                )
                newTransaction = oldTransaction.copy(
                    sourceAccount = secondSourceAccount,
                    amount = 1000.0f
                )
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSecondSourceAccount,
                    category = updatedCategory
                )
            }
            When("the user changes the account") {
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

            val category = Category(1L, "Transfer", Type.TRANSFER, 500.0f)
            val sourceAccount = Account(1L, "My Wallet", 500.0f)
            val destinationAccount = Account(2L, "Bank", 500.0f)

            val updatedCategory = Category(1L, "Transfer", Type.TRANSFER, 1000.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 0.0f)
            val updatedDestinationAccount = Account(2L, "Bank", 1000.0f)

            Given("a transfer transaction") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    destinationAccount,
                    500.0f,
                    "This month's savings"
                )
                newTransaction = oldTransaction.copy(amount = 1000.0f)
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSourceAccount,
                    category = updatedCategory,
                    destinationAccount = updatedDestinationAccount
                )
            }
            When("the user makes a transfer") {
                whenever(transactionRepository.insertOrUpdate(updatedTransaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedDestinationAccount))
                        .thenReturn(Completable.complete())

                updateTransactionUseCase.update(oldTransaction, newTransaction)
            }
            Then("the system should update the transaction") {
                verify(transactionRepository).insertOrUpdate(updatedTransaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
            And("the system should update the destination account's balance") {
                verify(accountRepository).insertOrUpdate(updatedDestinationAccount)
            }
        }

        Scenario("The user wants to make a transfer without enough balance") {

            val category = Category(1L, "Transfer", Type.TRANSFER, 500.0f)
            val sourceAccount = Account(1L, "My Wallet", 500.0f)
            val destinationAccount = Account(2L, "Bank", 500.0f)

            val updatedCategory = Category(1L, "Transfer", Type.TRANSFER, 1500.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", -500.0f)
            val updatedDestinationAccount = Account(2L, "Bank", 1500.0f)

            Given("a transfer transaction") {
                oldTransaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    destinationAccount,
                    500.0f,
                    "This month's savings"
                )
                newTransaction = oldTransaction.copy(amount = 1500.0f)
                updatedTransaction = newTransaction.copy(
                    sourceAccount = updatedSourceAccount,
                    category = updatedCategory,
                    destinationAccount = updatedDestinationAccount
                )
            }
            When("the user makes a transfer") {
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

    Feature("Delete Transaction") {

        val transactionRepository by memoized { mock<TransactionRepository>() }
        val categoryRepository by memoized { mock<CategoryRepository>() }
        val accountRepository by memoized { mock<AccountRepository>() }

        val deleteTransactionUseCase by memoized {
            DeleteTransactionUseCase(transactionRepository, categoryRepository, accountRepository)
        }

        Scenario("The user wants to delete an income transaction") {

            val category = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)

            val updatedCategory = Category(1L, "Salary", Type.INCOME, 0.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 0.0f)

            lateinit var transaction: Transaction

            Given("an income transaction") {
                transaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
            }
            When("the user deletes the transaction") {
                whenever(transactionRepository.delete(transaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())

                deleteTransactionUseCase.delete(transaction)
            }
            Then("the system should delete the transaction") {
                verify(transactionRepository).delete(transaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to delete an expense transaction") {

            val category = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            val sourceAccount = Account(1L, "My Wallet", 0.0f)

            val updatedCategory = Category(1L, "Food", Type.EXPENSE, 0.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 1000.0f)

            lateinit var transaction: Transaction

            Given("an expense transaction") {
                transaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "Today's groceries"
                )
            }
            When("the user deletes the transaction") {
                whenever(transactionRepository.delete(transaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())

                deleteTransactionUseCase.delete(transaction)
            }
            Then("the system should delete the transaction") {
                verify(transactionRepository).delete(transaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to delete transfer transaction") {

            val category = Category(1L, "Transfer", Type.TRANSFER, 1000.0f)
            val sourceAccount = Account(1L, "My Wallet", 0.0f)
            val destinationAccount = Account(2L, "Bank", 1000.0f)

            val updatedCategory = Category(1L, "Transfer", Type.TRANSFER, 0.0f)
            val updatedSourceAccount = Account(1L, "My Wallet", 1000.0f)
            val updatedDestinationAccount = Account(2L, "Bank", 0.0f)

            lateinit var transaction: Transaction

            Given("an expense transaction") {
                transaction = Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    destinationAccount,
                    1000.0f,
                    "This month's savings"
                )
            }
            When("the user deletes the transaction") {
                whenever(transactionRepository.delete(transaction))
                        .thenReturn(Completable.complete())
                whenever(categoryRepository.insertOrUpdate(updatedCategory))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                        .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedDestinationAccount))
                        .thenReturn(Completable.complete())

                deleteTransactionUseCase.delete(transaction)
            }
            Then("the system should delete the transaction") {
                verify(transactionRepository).delete(transaction)
            }
            And("the system should update the specified category's total") {
                verify(categoryRepository).insertOrUpdate(updatedCategory)
            }
            And("the system should update the specified source account's balance") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
            And("the system should update the specified destination account's balance") {
                verify(accountRepository).insertOrUpdate(updatedDestinationAccount)
            }
        }
    }
})
