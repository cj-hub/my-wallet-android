package com.cjhub.domain.usecases.account

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.models.Account

/**
 * Show all the accounts stored in the database.
 */
class ShowAccountsUseCase(private val accountRepository: AccountRepository) {

    fun show(): Single<List<Account>> = TODO()
}
