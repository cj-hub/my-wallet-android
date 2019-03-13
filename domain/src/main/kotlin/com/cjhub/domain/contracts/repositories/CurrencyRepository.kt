package com.cjhub.domain.contracts.repositories

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

import com.cjhub.domain.models.Currency

/**
 * Currency repository interface for data layer.
 */
interface CurrencyRepository {

    fun getAll(): Single<List<Currency>>

    fun findActive(): Maybe<Currency>

    fun update(currency: Currency): Completable
}
