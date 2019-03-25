package com.cjhub.domain.usecases.account

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.usecases.utilities.ShowAccountsUseCase

/**
 * Show all the accounts stored in the database.
 */
class ShowAccountsUseCase(private val accountRepository: AccountRepository) {

    fun show(): Single<List<Account>> = ShowAccountsUseCase(accountRepository).show()
}
