package com.cjhub.domain.usecases.account

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.models.Account

/**
 * Create a new sourceAccount and store it in the database.
 */
class CreateAccountUseCase(private val accountRepository: AccountRepository) {

    fun create(account: Account): Completable = TODO()
}
