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
import com.cjhub.domain.models.Currency

import com.cjhub.data.dao.CurrencyDaoImpl
import com.cjhub.data.entities.CurrencyEntity

/**
 * Spek tests for Currency repository.
 */
class CurrencyRepositoryFeature : Spek({

    Feature("Get All Currencies") {

        val currencyDao by memoized { mock<CurrencyDaoImpl>() }
        val currencyMapper by memoized { mock<Mapper<CurrencyEntity, Currency>>() }

        val currencyRepository by memoized { CurrencyRepositoryImpl(currencyDao, currencyMapper) }

        Scenario("The system wants to get the list of currencies") {

            val firstCurrencyEntity = CurrencyEntity(
                1L,
                "USD",
                "$",
                true,
                System.currentTimeMillis()
            )
            val secondCurrencyEntity = CurrencyEntity(
                2L,
                "EUR",
                "€",
                false,
                System.currentTimeMillis()
            )
            val thirdCurrencyEntity = CurrencyEntity(
                3L,
                "GBP",
                "£",
                false,
                System.currentTimeMillis()
            )

            val firstCurrencyModel = Currency(1L, "USD", "$", true)
            val secondCurrencyModel = Currency(2L, "EUR", "€", false)
            val thirdCurrencyModel = Currency(3L, "GBP", "£", false)

            lateinit var currencyEntities: List<CurrencyEntity>

            lateinit var testObserver: TestObserver<List<Currency>>

            Given("a list of currencies exists") {
                currencyEntities =
                        listOf(firstCurrencyEntity, secondCurrencyEntity, thirdCurrencyEntity)
            }
            When("the system gets the list of currencies") {
                whenever(currencyDao.getAll()).thenReturn(Single.just(currencyEntities))
                whenever(currencyMapper.toModel(firstCurrencyEntity)).thenReturn(firstCurrencyModel)
                whenever(currencyMapper.toModel(secondCurrencyEntity)).thenReturn(secondCurrencyModel)
                whenever(currencyMapper.toModel(thirdCurrencyEntity)).thenReturn(thirdCurrencyModel)

                testObserver = currencyRepository.getAll().test()
            }
            Then("the system should get the list successfully") {
                testObserver.assertComplete()
            }
            And("the list of entities should be mapped accordingly") {
                currencyEntities.forEach { currencyEntity ->
                    verify(currencyMapper).toModel(currencyEntity)
                }
            }
            And("the list of currencies should be returned") {
                testObserver.assertResult(listOf(
                    firstCurrencyModel, secondCurrencyModel, thirdCurrencyModel
                ))
            }
        }
    }

    Feature("Update Currency") {

        val currencyDao by memoized { mock<CurrencyDaoImpl>() }
        val currencyMapper by memoized { mock<Mapper<CurrencyEntity, Currency>>() }

        val currencyRepository by memoized { CurrencyRepositoryImpl(currencyDao, currencyMapper) }

        Scenario("The system wants to update a currency") {

            val currencyEntity = CurrencyEntity(
                1L,
                "USD",
                "$",
                false,
                System.currentTimeMillis()
            )

            lateinit var currencyModel: Currency

            lateinit var testObserver: TestObserver<Void>

            Given("a currency to be updated") {
                currencyModel = Currency(1L, "USD", "$", false)
            }
            When("the system updates the currency") {
                whenever(currencyMapper.toEntity(currencyModel)).thenReturn(currencyEntity)

                testObserver = currencyRepository.update(currencyModel).test()
            }
            Then("the system should update successfully") {
                testObserver.assertComplete()
            }
            And("the model should be mapped accordingly") {
                argumentCaptor<CurrencyEntity> {
                    verify(currencyDao).update(capture())
                    assert(firstValue == currencyEntity)
                }
            }
        }
    }
})
