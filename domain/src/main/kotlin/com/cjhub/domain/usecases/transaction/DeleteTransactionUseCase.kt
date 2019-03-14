package com.cjhub.domain.usecases.transaction

import io.reactivex.Completable

import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.contracts.repositories.CategoryRepository
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Transaction

/**
 * Delete an existing transaction from the database.
 */
class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun delete(transaction: Transaction): Completable = TODO()
}
