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
                verify(accountRepository).insertOrUpdate(sourceAccount)
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
})
