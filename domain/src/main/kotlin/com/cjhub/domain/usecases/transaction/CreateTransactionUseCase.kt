package com.cjhub.domain.usecases.transaction

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Transaction

/**
 * Create a new transaction and store it in the database.
 */
class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun create(transaction: Transaction): Completable = TODO()
}
