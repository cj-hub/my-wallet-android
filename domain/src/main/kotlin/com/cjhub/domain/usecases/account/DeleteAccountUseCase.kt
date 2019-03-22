package com.cjhub.domain.usecases.account

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Transaction

/**
 * Delete an existing account from the database.
 */
class DeleteAccountUseCase(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
) {

    fun delete(account: Account, relatedTransactions: List<Transaction>): Completable = TODO()
}
