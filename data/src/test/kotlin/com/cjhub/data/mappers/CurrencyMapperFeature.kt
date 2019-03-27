package com.cjhub.data.mappers

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

import com.cjhub.domain.models.Currency

import com.cjhub.data.entities.CurrencyEntity

/**
 * Spek tests for Currency mapper.
 */
class CurrencyMapperFeature : Spek({

    Feature("Currency Entity Mapper") {

        val currencyMapper by memoized { CurrencyMapperImpl() }

        Scenario("The system wants to map a currency model to a corresponding entity") {

            lateinit var currencyModel: Currency

            lateinit var currencyEntity: CurrencyEntity

            Given("a currency model") {
                currencyModel = Currency(1L, "USD", "$", true)
            }
            When("the system maps to a currency entity") {
                currencyEntity = currencyMapper.toEntity(currencyModel)
            }
            Then("a valid currency entity should be returned") {
                assert(currencyEntity.id == 1L)
                assert(currencyEntity.name == "USD")
                assert(currencyEntity.symbol == "$")
                assert(currencyEntity.isActive)
                assert(currencyEntity.timestamp > 0L)
            }
        }
    }

    Feature("Currency Model Mapper") {

        val currencyMapper by memoized { CurrencyMapperImpl() }

        Scenario("The system wants to map a currency entity to a corresponding model") {

            lateinit var currencyEntity: CurrencyEntity

            lateinit var currencyModel: Currency

            Given("a currency entity") {
                currencyEntity = CurrencyEntity(1L, "USD", "$", true, System.currentTimeMillis())
            }
            When("the system maps to a currency model") {
                currencyModel = currencyMapper.toModel(currencyEntity)
            }
            Then("a valid currency model should be returned") {
                assert(currencyModel.id == 1L)
                assert(currencyModel.name == "USD")
                assert(currencyModel.symbol == "$")
                assert(currencyModel.isActive)
            }
        }
    }
})
