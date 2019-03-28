package com.cjhub.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.contracts.repositories.CurrencyRepository
import com.cjhub.domain.models.Currency

import com.cjhub.data.dao.CurrencyDaoImpl
import com.cjhub.data.entities.CurrencyEntity

/**
 * Room implementation of Currency repository.
 */
class CurrencyRepositoryImpl(
    private val currencyDao: CurrencyDaoImpl,
    private val currencyMapper: Mapper<CurrencyEntity, Currency>
) : CurrencyRepository {

    override fun getAll(): Single<List<Currency>> = TODO()

    override fun update(currency: Currency): Completable = TODO()
}
