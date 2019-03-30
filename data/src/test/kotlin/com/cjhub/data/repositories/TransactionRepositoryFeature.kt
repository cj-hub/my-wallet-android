package com.cjhub.data.repositories

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import io.reactivex.Single
import io.reactivex.observers.TestObserver

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction
import com.cjhub.domain.models.Type

import com.cjhub.data.dao.TransactionDaoImpl
import com.cjhub.data.entities.AccountEntity
import com.cjhub.data.entities.CategoryEntity
import com.cjhub.data.entities.DetailedTransactionEntity
import com.cjhub.data.entities.TransactionEntity

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Spek tests for Transaction repository.
 */
class TransactionRepositoryFeature : Spek({

    Feature("Get Transactions") {

        val transactionDao by memoized { mock<TransactionDaoImpl>() }
        val transactionMapper by memoized { mock<Mapper<TransactionEntity, Transaction>>() }
        val detailedTransactionMapper by memoized { mock<Mapper<DetailedTransactionEntity, Transaction>>() }

        val transactionRepository by memoized {
            TransactionRepositoryImpl(transactionDao, transactionMapper, detailedTransactionMapper)
        }

        val dateTimeMilli = System.currentTimeMillis()
        val dateTime = Instant.ofEpochMilli(dateTimeMilli).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val firstCategoryEntity = CategoryEntity(
            1L,
            "Salary",
            "Income",
            1500.0f,
            System.currentTimeMillis()
        )
        val secondCategoryEntity = CategoryEntity(
            2L,
            "Food",
            "Expense",
            500.0f,
            System.currentTimeMillis()
        )
        val firstAccountEntity = AccountEntity(
            1L,
            "My Wallet",
            1000.0f,
            System.currentTimeMillis()
        )
        val secondAccountEntity = AccountEntity(
            2L,
            "Bank",
            0.0f,
            System.currentTimeMillis()
        )
        val firstTransactionEntity = DetailedTransactionEntity(
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
            firstCategoryEntity,
            firstAccountEntity,
            null
        )
        val secondTransactionEntity = DetailedTransactionEntity(
            TransactionEntity(
                2L,
                1L,
                2L,
                null,
                dateTimeMilli,
                500.0f,
                "This month's bonus",
                System.currentTimeMillis()
            ),
            firstCategoryEntity,
            secondAccountEntity,
            null
        )
        val thirdTransactionEntity = DetailedTransactionEntity(
            TransactionEntity(
                3L,
                2L,
                2L,
                null,
                dateTimeMilli,
                500.0f,
                "Today's groceries",
                System.currentTimeMillis()
            ),
            secondCategoryEntity,
            secondAccountEntity,
            null
        )

        val firstCategoryModel = Category(1L, "Salary", Type.INCOME, 1500.0f)
        val secondCategoryModel = Category(2L, "Food", Type.EXPENSE, 500.0f)
        val firstAccountModel = Account(1L, "My Wallet", 1000.0f)
        val secondAccountModel = Account(2L, "Bank", 0.0f)
        val firstTransactionModel = Transaction(
            1L,
            dateTime,
            firstAccountModel,
            firstCategoryModel,
            Account.NO_ACCOUNT,
            1000.0f,
            "This month's salary"
        )
        val secondTransactionModel = Transaction(
            2L,
            dateTime,
            secondAccountModel,
            firstCategoryModel,
            Account.NO_ACCOUNT,
            500.0f,
            "This month's bonus"
        )
        val thirdTransactionModel = Transaction(
            3L,
            dateTime,
            secondAccountModel,
            secondCategoryModel,
            Account.NO_ACCOUNT,
            500.0f,
            "Today's groceries"
        )

        lateinit var transactionEntities: List<DetailedTransactionEntity>

        lateinit var testObserver: TestObserver<List<Transaction>>

        Scenario("The system wants to get the list of transactions") {

            Given("a list of transactions exists") {
                transactionEntities = listOf(firstTransactionEntity, secondTransactionEntity, thirdTransactionEntity)
            }
            When("the system gets the list of transactions") {
                whenever(transactionDao.getAll())
                        .thenReturn(Single.just(transactionEntities))
                whenever(detailedTransactionMapper.toModel(firstTransactionEntity))
                        .thenReturn(firstTransactionModel)
                whenever(detailedTransactionMapper.toModel(secondTransactionEntity))
                        .thenReturn(secondTransactionModel)
                whenever(detailedTransactionMapper.toModel(thirdTransactionEntity))
                        .thenReturn(thirdTransactionModel)

                testObserver = transactionRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                transactionEntities.forEach { transactionEntity ->
                    verify(detailedTransactionMapper).toModel(transactionEntity)
                }
            }
            And("the list of transactions should be returned") {
                testObserver.assertResult(listOf(
                    firstTransactionModel, secondTransactionModel, thirdTransactionModel
                ))
            }
        }

        Scenario("The system wants to get the list of transactions with a specific account") {

            Given("a list of transactions with a specific account exists") {
                transactionEntities = listOf(firstTransactionEntity)
            }
            When("the system gets the list of transactions with a specific account") {
                whenever(transactionDao.getAll())
                        .thenReturn(Single.just(transactionEntities))
                whenever(detailedTransactionMapper.toModel(firstTransactionEntity))
                        .thenReturn(firstTransactionModel)

                testObserver = transactionRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                verify(detailedTransactionMapper).toModel(firstTransactionEntity)
            }
            And("the list of transactions should be returned") {
                testObserver.assertResult(listOf(firstTransactionModel))
            }
        }

        Scenario("The system wants to get the list of transactions with a specific category") {

            Given("a list of transactions with a specific category exists") {
                transactionEntities = listOf(firstTransactionEntity, secondTransactionEntity)
            }
            When("the system gets the list of transactions with a specific category") {
                whenever(transactionDao.getAll())
                        .thenReturn(Single.just(transactionEntities))
                whenever(detailedTransactionMapper.toModel(firstTransactionEntity))
                        .thenReturn(firstTransactionModel)
                whenever(detailedTransactionMapper.toModel(secondTransactionEntity))
                        .thenReturn(secondTransactionModel)

                testObserver = transactionRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                transactionEntities.forEach { transactionEntity ->
                    verify(detailedTransactionMapper).toModel(transactionEntity)
                }
            }
            And("the list of transactions should be returned") {
                testObserver.assertResult(listOf(firstTransactionModel, secondTransactionModel))
            }
        }
    }

    Feature("Insert or Update Transaction") {

        val transactionDao by memoized { mock<TransactionDaoImpl>() }
        val transactionMapper by memoized { mock<Mapper<TransactionEntity, Transaction>>() }
        val detailedTransactionMapper by memoized { mock<Mapper<DetailedTransactionEntity, Transaction>>() }

        val transactionRepository by memoized {
            TransactionRepositoryImpl(transactionDao, transactionMapper, detailedTransactionMapper)
        }

        Scenario("The system wants to insert or update a transaction") {

            val dateTime = LocalDateTime.now()
            val dateTimeMilli = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val transactionEntity = TransactionEntity(
                1L,
                1L,
                1L,
                null,
                dateTimeMilli,
                1000.0f,
                "This month's salary",
                System.currentTimeMillis()
            )

            lateinit var transactionModel: Transaction

            lateinit var testObserver: TestObserver<Void>

            Given("a transaction to be inserted or updated") {
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
            When("the system inserts or updates the transaction") {
                whenever(transactionMapper.toEntity(transactionModel)).thenReturn(transactionEntity)

                testObserver = transactionRepository.insertOrUpdate(transactionModel).test()
            }
            Then("the system should insert or update successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<TransactionEntity> {
                    verify(transactionDao).insertOrUpdate(capture())
                    assert(firstValue == transactionEntity)
                }
            }
        }
    }

    Feature("Delete Transaction") {

        val transactionDao by memoized { mock<TransactionDaoImpl>() }
        val transactionMapper by memoized { mock<Mapper<TransactionEntity, Transaction>>() }
        val detailedTransactionMapper by memoized { mock<Mapper<DetailedTransactionEntity, Transaction>>() }

        val transactionRepository by memoized {
            TransactionRepositoryImpl(transactionDao, transactionMapper, detailedTransactionMapper)
        }

        Scenario("The system wants to delete a transaction") {

            val dateTime = LocalDateTime.now()
            val dateTimeMilli = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val transactionEntity = TransactionEntity(
                1L,
                1L,
                1L,
                null,
                dateTimeMilli,
                1000.0f,
                "This month's salary",
                System.currentTimeMillis()
            )

            lateinit var transactionModel: Transaction

            lateinit var testObserver: TestObserver<Void>

            Given("a transaction to be inserted or updated") {
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
            When("the system deletes the transaction") {
                whenever(transactionMapper.toEntity(transactionModel)).thenReturn(transactionEntity)

                testObserver = transactionRepository.delete(transactionModel).test()
            }
            Then("the system should delete successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<TransactionEntity> {
                    verify(transactionDao).delete(capture())
                    assert(firstValue == transactionEntity)
                }
            }
        }
    }

    Feature("Clear Transactions") {

        val transactionDao by memoized { mock<TransactionDaoImpl>() }
        val transactionMapper by memoized { mock<Mapper<TransactionEntity, Transaction>>() }
        val detailedTransactionMapper by memoized { mock<Mapper<DetailedTransactionEntity, Transaction>>() }

        val transactionRepository by memoized {
            TransactionRepositoryImpl(transactionDao, transactionMapper, detailedTransactionMapper)
        }

        Scenario("The system wants to clear all transactions") {

            lateinit var testObserver: TestObserver<Void>

            When("the system clears all transactions") {
                testObserver = transactionRepository.clear().test()
            }
            Then("the system should clear successfully") {
                testObserver.assertComplete()
            }
        }
    }
})
