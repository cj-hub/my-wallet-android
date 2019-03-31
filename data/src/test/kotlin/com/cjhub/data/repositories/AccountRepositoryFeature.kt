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

import com.cjhub.data.dao.AccountDaoImpl
import com.cjhub.data.entities.AccountEntity

/**
 * Spek tests for Account repository.
 */
class AccountRepositoryFeature : Spek({

    Feature("Get Accounts") {

        val accountDao by memoized { mock<AccountDaoImpl>() }
        val accountMapper by memoized { mock<Mapper<AccountEntity?, Account>>() }

        val accountRepository by memoized { AccountRepositoryImpl(accountDao, accountMapper) }

        val firstAccountEntity = AccountEntity(
            1L,
            "My Wallet",
            1000.0f,
            System.currentTimeMillis()
        )
        val secondAccountEntity = AccountEntity(
            2L,
            "Bank",
            500.0f,
            System.currentTimeMillis()
        )

        val firstAccountModel = Account(1L, "My Wallet", 1000.0f)
        val secondAccountModel = Account(2L, "Bank", 500.0f)

        lateinit var accountEntities: List<AccountEntity>

        lateinit var testObserver: TestObserver<List<Account>>

        Scenario("The system wants to get the list of accounts") {

            Given("a list of accounts exists") {
                accountEntities = listOf(firstAccountEntity, secondAccountEntity)
            }
            When("the system get the list of accounts") {
                whenever(accountDao.getAll()).thenReturn(Single.just(accountEntities))
                whenever(accountMapper.toModel(firstAccountEntity)).thenReturn(firstAccountModel)
                whenever(accountMapper.toModel(secondAccountEntity)).thenReturn(secondAccountModel)

                testObserver = accountRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                accountEntities.forEach { accountEntity ->
                    verify(accountMapper).toModel(accountEntity)
                }
            }
            And("the list of accounts should be returned") {
                testObserver.assertResult(listOf(firstAccountModel, secondAccountModel))
            }
        }
    }

    Feature("Insert or Update Account") {

        val accountDao by memoized { mock<AccountDaoImpl>() }
        val accountMapper by memoized { mock<Mapper<AccountEntity?, Account>>() }

        val accountRepository by memoized { AccountRepositoryImpl(accountDao, accountMapper) }

        Scenario("The system wants to insert or update an account") {

            val accountEntity = AccountEntity(
                1L,
                "My Wallet",
                1000.0f,
                System.currentTimeMillis()
            )

            lateinit var accountModel: Account

            lateinit var testObserver: TestObserver<Void>

            Given("an account to be inserted or updated") {
                accountModel = Account(1L, "My Wallet", 1000.0f)
            }
            When("the system inserts or updates the account") {
                whenever(accountMapper.toEntity(accountModel)).thenReturn(accountEntity)

                testObserver = accountRepository.insertOrUpdate(accountModel).test()
            }
            Then("the system should insert or update successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<AccountEntity> {
                    verify(accountDao).insertOfUpdate(capture())
                    assert(firstValue == accountEntity)
                }
            }
        }
    }

    Feature("Delete Account") {

        val accountDao by memoized { mock<AccountDaoImpl>() }
        val accountMapper by memoized { mock<Mapper<AccountEntity?, Account>>() }

        val accountRepository by memoized { AccountRepositoryImpl(accountDao, accountMapper) }

        Scenario("The system wants to delete an account") {

            val accountEntity = AccountEntity(
                1L,
                "My Wallet",
                1000.0f,
                System.currentTimeMillis()
            )

            lateinit var accountModel: Account

            lateinit var testObserver: TestObserver<Void>

            Given("an account to be deleted") {
                accountModel = Account(1L, "My Wallet", 1000.0f)
            }
            When("the system deletes the account") {
                whenever(accountMapper.toEntity(accountModel)).thenReturn(accountEntity)

                testObserver = accountRepository.delete(accountModel).test()
            }
            Then("the system should delete successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<AccountEntity> {
                    verify(accountDao).delete(capture())
                    assert(firstValue == accountEntity)
                }
            }
        }
    }

    Feature("Reset Accounts") {

        val accountDao by memoized { mock<AccountDaoImpl>() }
        val accountMapper by memoized { mock<Mapper<AccountEntity?, Account>>() }

        val accountRepository by memoized { AccountRepositoryImpl(accountDao, accountMapper) }

        Scenario("The system wants to reset all accounts") {

            lateinit var testObserver: TestObserver<Void>

            When("the system resets all accounts") {
                testObserver = accountRepository.reset().test()
            }
            Then("the system should reset successfully") {
                testObserver.assertComplete()
            }
        }
    }
})
