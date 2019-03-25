package com.cjhub.domain.usecases.settings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.verifyZeroInteractions

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
 * Spek tests for various category use cases.
 */
object CategoryFeatures : Spek({

    Feature("Show Categories") {

        val categoryRepository by memoized { mock<CategoryRepository>() }

        val showCategoriesUseCase by memoized { ShowCategoriesUseCase(categoryRepository) }

        Scenario("The user wants to request for the categories") {

            When("the user requests for the categories") {
                showCategoriesUseCase.show()
            }
            Then("the system should get the list of categories") {
                verify(categoryRepository).getAll()
            }
        }
    }

    Feature("Create Category") {

        val categoryRepository by memoized { mock<CategoryRepository>() }

        val createCategoryUseCase by memoized { CreateCategoryUseCase(categoryRepository) }

        lateinit var newCategory: Category

        Scenario("The user wants to create a new category") {

            Given("a new category") {
                newCategory = Category(1L, "Salary", Type.INCOME, 0.0f)
            }
            When("the user creates a new category") {
                whenever(categoryRepository.insertOrUpdate(newCategory))
                        .thenReturn(Completable.complete())

                createCategoryUseCase.create(newCategory)
            }
            Then("the system should insert the new category") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
        }
    }

    Feature("Update Category") {

        val categoryRepository by memoized { mock<CategoryRepository>() }
        val accountRepository by memoized { mock<AccountRepository>() }

        val updateCategoryUseCase by memoized {
            UpdateCategoryUseCase(categoryRepository, accountRepository)
        }

        lateinit var newCategory: Category

        Scenario("The user wants to update a category within the same type") {

            val category = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val sourceAccount = Account(1L, "My Wallet", 2000.0f)

            val transactions = listOf(
                Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's salary"
                ),
                Transaction(
                    2L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    300.0f,
                    "This month's extra cash"
                ),
                Transaction(
                    3L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    200.0f,
                    "This month's bonus"
                )
            )

            Given("a new category") {
                newCategory = Category(1L, "Bonus", Type.INCOME, 1000.0f)
            }
            When("the user updates a category") {
                whenever(categoryRepository.insertOrUpdate(newCategory))
                    .thenReturn(Completable.complete())

                updateCategoryUseCase.update(category, newCategory, transactions)
            }
            Then("the system should update the category") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should not update any account") {
                verifyZeroInteractions(accountRepository)
            }
        }

        Scenario("The user wants to update a category to a different type, with enough balance") {

            val category = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val sourceAccount = Account(1L, "My Wallet", 2000.0f)

            val updatedSourceAccount = Account(1L, "My Wallet", 0.0f)

            val transactions = listOf(
                Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's salary"
                ),
                Transaction(
                    2L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    300.0f,
                    "This month's extra cash"
                ),
                Transaction(
                    3L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    200.0f,
                    "This month's bonus"
                )
            )

            Given("a new category") {
                newCategory = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            }
            When("the user updates a category") {
                whenever(categoryRepository.insertOrUpdate(newCategory))
                    .thenReturn(Completable.complete())
                whenever(accountRepository.insertOrUpdate(updatedSourceAccount))
                    .thenReturn(Completable.complete())

                updateCategoryUseCase.update(category, newCategory, transactions)
            }
            Then("the system should update the category") {
                verify(categoryRepository).insertOrUpdate(newCategory)
            }
            And("the system should update the specified source account") {
                verify(accountRepository).insertOrUpdate(updatedSourceAccount)
            }
        }

        Scenario("The user wants to update a category to a different type, without enough balance") {

            val category = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)

            val transactions = listOf(
                Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    500.0f,
                    "This month's salary"
                ),
                Transaction(
                    2L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    300.0f,
                    "This month's extra cash"
                ),
                Transaction(
                    3L,
                    LocalDateTime.now(),
                    sourceAccount,
                    category,
                    Account.NO_ACCOUNT,
                    200.0f,
                    "This month's bonus"
                )
            )

            Given("a new category") {
                newCategory = Category(1L, "Food", Type.EXPENSE, 1000.0f)
            }
            When("the user updates a category") {
                updateCategoryUseCase.update(category, newCategory, transactions)
            }
            Then("the system should not update the category") {
                verifyZeroInteractions(categoryRepository)
            }
            And("the system should not update the specified source account") {
                verifyZeroInteractions(accountRepository)
            }
        }
    }

    Feature("Delete Transaction") {

        val categoryRepository by memoized { mock<CategoryRepository>() }
        val transactionRepository by memoized { mock<TransactionRepository>() }

        val deleteCategoryUseCase by memoized {
            DeleteCategoryUseCase(categoryRepository, transactionRepository)
        }

        lateinit var category: Category
        lateinit var newTransactions: List<Transaction>

        Scenario("The user wants to delete an income category") {

            val currentCategory = Category(3L, "Salary", Type.INCOME, 1000.0f)
            val otherIncome = Category(1L, "Other", Type.INCOME, 0.0f)
            val otherExpense = Category(2L, "Other", Type.EXPENSE, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)
            val transactions = listOf(
                Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    currentCategory,
                    Account.NO_ACCOUNT,
                    750.0f,
                    "This month's salary"
                ),
                Transaction(
                    2L,
                    LocalDateTime.now(),
                    sourceAccount,
                    currentCategory,
                    Account.NO_ACCOUNT,
                    250.0f,
                    "This month's bonus"
                )
            )

            val updatedOtherIncome = Category(1L, "Other", Type.INCOME, 1000.0f)

            Given("an income category") {
                category = currentCategory
                newTransactions = transactions.map { transaction ->
                    transaction.copy(category = updatedOtherIncome)
                }
            }
            When("the user deletes an income category") {
                whenever(categoryRepository.insertOrUpdate(updatedOtherIncome))
                        .thenReturn(Completable.complete())
                newTransactions.forEach { newTransaction ->
                    whenever(transactionRepository.insertOrUpdate(newTransaction))
                            .thenReturn(Completable.complete())
                }
                whenever(categoryRepository.delete(category))
                        .thenReturn(Completable.complete())

                deleteCategoryUseCase.delete(category, transactions, otherIncome, otherExpense)
            }
            Then("the system should update the corresponding 'Other' category") {
                verify(categoryRepository).insertOrUpdate(updatedOtherIncome)
            }
            And("the system should update all the transactions") {
                newTransactions.forEach { newTransaction ->
                    verify(transactionRepository).insertOrUpdate(newTransaction)
                }
            }
            And("the system should delete the category") {
                verify(categoryRepository).delete(category)
            }
        }

        Scenario("The user wants to delete an expense category") {

            val currentCategory = Category(3L, "Food", Type.EXPENSE, 1000.0f)
            val otherIncome = Category(1L, "Other", Type.INCOME, 0.0f)
            val otherExpense = Category(2L, "Other", Type.EXPENSE, 0.0f)
            val sourceAccount = Account(1L, "My Wallet", 1000.0f)
            val transactions = listOf(
                Transaction(
                    1L,
                    LocalDateTime.now(),
                    sourceAccount,
                    currentCategory,
                    Account.NO_ACCOUNT,
                    750.0f,
                    "Today's groceries"
                ),
                Transaction(
                    2L,
                    LocalDateTime.now(),
                    sourceAccount,
                    currentCategory,
                    Account.NO_ACCOUNT,
                    250.0f,
                    "Tonight's dinner"
                )
            )

            val updatedOtherExpense = Category(2L, "Other", Type.EXPENSE, 1000.0f)

            Given("an expense category") {
                category = currentCategory
                newTransactions = transactions.map { transaction ->
                    transaction.copy(category = updatedOtherExpense)
                }
            }
            When("the user deletes an expense category") {
                whenever(categoryRepository.insertOrUpdate(updatedOtherExpense))
                        .thenReturn(Completable.complete())
                newTransactions.forEach { newTransaction ->
                    whenever(transactionRepository.insertOrUpdate(newTransaction))
                            .thenReturn(Completable.complete())
                }
                whenever(categoryRepository.delete(category))
                        .thenReturn(Completable.complete())

                deleteCategoryUseCase.delete(category, transactions, otherIncome, otherExpense)
            }
            Then("the system should update the corresponding 'Other' category") {
                verify(categoryRepository).insertOrUpdate(updatedOtherExpense)
            }
            And("the system should update all the transactions") {
                newTransactions.forEach { newTransaction ->
                    verify(transactionRepository).insertOrUpdate(newTransaction)
                }
            }
            And("the system should delete the category") {
                verify(categoryRepository).delete(category)
            }
        }
    }
})
