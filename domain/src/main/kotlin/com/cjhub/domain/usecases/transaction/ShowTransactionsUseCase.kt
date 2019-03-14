package com.cjhub.domain.usecases.transaction

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Transaction

/**
 * Show all the transactions stored in the database.
 */
class ShowTransactionsUseCase(private val transactionRepository: TransactionRepository) {

    fun show(): Single<List<Transaction>> = TODO()
}
