package com.cjhub.domain.usecases.utilities

import io.reactivex.Single

import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction

/**
 * Show all the transactions corresponding to a category.
 */
class ShowTransactionsByCategoryUseCase(private val transactionRepository: TransactionRepository) {

    fun showBy(category: Category): Single<List<Transaction>> = TODO()
}