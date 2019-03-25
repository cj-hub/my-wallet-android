package com.cjhub.domain.usecases.settings

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.CurrencyRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Currency

/**
 * Select the currency to be used from the database.
 */
class SelectCurrencyUseCase(
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun select(oldCurrency: Currency, newCurrency: Currency): Completable {
        return if (oldCurrency == newCurrency) {
            Completable.error(IllegalArgumentException(
                "Currency already selected"
            ))
        } else {
            currencyRepository.update(oldCurrency.copy(isActive = false))
                    .andThen(currencyRepository.update(newCurrency))
                    .andThen(transactionRepository.clear())
                    .andThen(categoryRepository.reset())
                    .andThen(accountRepository.reset())
        }
    }
}
