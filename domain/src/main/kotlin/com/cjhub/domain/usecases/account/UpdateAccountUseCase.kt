package com.cjhub.domain.usecases.account

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.models.Account

/**
 * Update an existing account and store it in the database.
 */
class UpdateAccountUseCase(private val accountRepository: AccountRepository) {

    fun update(account: Account): Completable = TODO()
}
