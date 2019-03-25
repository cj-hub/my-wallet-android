package com.cjhub.domain.usecases.utilities

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Transaction

/**
 * Show all the transactions corresponding to an account.
 */
class ShowTransactionsByAccountUseCase(private val transactionRepository: TransactionRepository) {

    fun showBy(account: Account): Single<List<Transaction>> = transactionRepository.getAllByAccount(account)
}
