package com.cjhub.data.mappers

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.cjhub.domain.models.Account

import com.cjhub.data.entities.AccountEntity

/**
 * Spek tests for Account mapper.
 */
class AccountMapperFeature : Spek({

    Feature("Account Entity Mapper") {

        val accountMapper by memoized { AccountMapperImpl() }

        Scenario("The system wants to map an account model to a corresponding entity") {

            lateinit var accountModel: Account

            var accountEntity: AccountEntity? = null

            Given("an account model") {
                accountModel = Account(1L, "My Wallet", 1000.0f)
            }
            When("the system maps to an account entity") {
                accountEntity = accountMapper.toEntity(accountModel)
            }
            Then("a valid account entity should be returned") {
                assert(accountEntity!!.id == 1L)
                assert(accountEntity!!.name == "My Wallet")
                assert(accountEntity!!.balance == 1000.0f)
                assert(accountEntity!!.timestamp > 0L)
            }
        }

        Scenario("The system wants to map a no-account model to a corresponding entity") {

            lateinit var accountModel: Account

            var accountEntity: AccountEntity? = null

            Given("an no-account model") {
                accountModel = Account.NO_ACCOUNT
            }
            When("the system maps to an account entity") {
                accountEntity = accountMapper.toEntity(accountModel)
            }
            Then("a null entity should be returned") {
                assert(accountEntity == null)
            }
        }
    }

    Feature("Account Model Mapper") {

        val accountMapper by memoized { AccountMapperImpl() }

        Scenario("The system wants to map an account entity to a corresponding model") {

            var accountEntity: AccountEntity? = null

            lateinit var accountModel: Account

            Given("an account entity") {
                accountEntity = AccountEntity(1L, "My Wallet", 1000.0f, System.currentTimeMillis())
            }
            When("the system maps to an account model") {
                accountModel = accountMapper.toModel(accountEntity)
            }
            Then("a valid account model should be returned") {
                assert(accountModel.id == 1L)
                assert(accountModel.name == "My Wallet")
                assert(accountModel.balance == 1000.0f)
            }
        }

        Scenario("The system wants to map an null entity to a corresponding model") {

            var accountEntity: AccountEntity? = null

            lateinit var accountModel: Account

            Given("a null entity") {
                accountEntity = null
            }
            When("the system maps to an account model") {
                accountModel = accountMapper.toModel(accountEntity)
            }
            Then("a no-account model should be returned") {
                assert(accountModel == Account.NO_ACCOUNT)
            }
        }
    }
})
