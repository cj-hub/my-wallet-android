package com.cjhub.data.mappers

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

import com.cjhub.data.entities.AccountEntity
import com.cjhub.data.entities.CategoryEntity
import com.cjhub.data.entities.DetailedTransactionEntity
import com.cjhub.data.entities.TransactionEntity

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Spek tests for Transaction mapper.
 */
class TransactionMapperFeature : Spek({

    Feature("Transaction Entity Mapper") {

        val transactionMapper by memoized { TransactionMapperImpl() }

        Scenario("The system wants to map a transaction model to a corresponding entity") {

            val dateTime = LocalDateTime.now()
            val dateTimeMilli = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            lateinit var transactionModel: Transaction

            lateinit var transactionEntity: TransactionEntity

            Given("a transaction model") {
                transactionModel = Transaction(
                    1L,
                    dateTime,
                    Account(1L, "My Wallet", 1000.0f),
                    Category(1L, "Salary", Type.INCOME, 1000.0f),
                    Account.NO_ACCOUNT,
                    1000.0f,
                    "This month's salary"
                )
            }
            When("the system maps to a transaction entity") {
                transactionEntity = transactionMapper.toEntity(transactionModel)
            }
            Then("a valid transaction entity should be returned") {
                assert(transactionEntity.id == 1L)
                assert(transactionEntity.categoryId == 1L)
                assert(transactionEntity.sourceAccountId == 1L)
                assert(transactionEntity.destinationAccountId == null)
                assert(transactionEntity.dateTime == dateTimeMilli)
                assert(transactionEntity.amount == 1000.0f)
                assert(transactionEntity.description == "This month's salary")
                assert(transactionEntity.timestamp > 0L)
            }
        }
    }

    Feature("Transaction Model Mapper") {

        val categoryMapper by memoized { mock<Mapper<CategoryEntity, Category>>() }
        val accountMapper by memoized { mock<Mapper<AccountEntity, Account>>() }

        val transactionMapper by memoized {
            DetailedTransactionMapperImpl(categoryMapper, accountMapper)
        }

        Scenario("The system wants to map a transaction entity to a corresponding model") {

            val dateTimeMilli = System.currentTimeMillis()
            val dateTime = Instant.ofEpochMilli(dateTimeMilli).atZone(ZoneId.systemDefault()).toLocalDateTime()

            val categoryEntity = CategoryEntity(
                1L,
                "Salary",
                "Income",
                1000.0f,
                System.currentTimeMillis()
            )
            val accountEntity = AccountEntity(
                1L,
                "My Wallet",
                1000.0f,
                System.currentTimeMillis()
            )

            val categoryModel = Category(1L, "Salary", Type.INCOME, 1000.0f)
            val accountModel = Account(1L, "My Wallet", 1000.0f)

            lateinit var transactionEntity: DetailedTransactionEntity

            lateinit var transactionModel: Transaction

            Given("a transaction entity") {
                transactionEntity = DetailedTransactionEntity(
                    TransactionEntity(
                        1L,
                        1L,
                        1L,
                        null,
                        dateTimeMilli,
                        1000.0f,
                        "This month's salary",
                        System.currentTimeMillis()
                    ),
                    categoryEntity,
                    accountEntity,
                    null
                )
            }
            When("the system maps to a transaction model") {
                whenever(categoryMapper.toModel(categoryEntity)).thenReturn(categoryModel)
                whenever(accountMapper.toModel(accountEntity)).thenReturn(accountModel)

                transactionModel = transactionMapper.toModel(transactionEntity)
            }
            Then("a valid transaction model should be returned") {
                assert(transactionModel.id == 1L)
                assert(transactionModel.dateTime == dateTime)
                assert(transactionModel.sourceAccount == Account(1L, "My Wallet", 1000.0f))
                assert(transactionModel.category == Category(1L, "Salary", Type.INCOME, 1000.0f))
                assert(transactionModel.destinationAccount == Account.NO_ACCOUNT)
                assert(transactionModel.amount == 1000.0f)
                assert(transactionModel.description == "This month's salary")
            }
        }
    }
})
