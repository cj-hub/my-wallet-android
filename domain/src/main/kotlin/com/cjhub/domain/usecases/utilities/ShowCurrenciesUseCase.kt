package com.cjhub.domain.usecases.utilities

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.CurrencyRepository
import com.cjhub.domain.models.Currency

/**
 * Show all the currencies stored in the database.
 */
class ShowCurrenciesUseCase(private val currencyRepository: CurrencyRepository) {

    fun show(): Single<List<Currency>> = currencyRepository.getAll()
}
