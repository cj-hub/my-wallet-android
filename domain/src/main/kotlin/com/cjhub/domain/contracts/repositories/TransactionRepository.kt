package com.cjhub.domain.contracts.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.models.Transaction

/**
 * Transaction repository interface for data layer.
 */
interface TransactionRepository {

    fun getAll(): Single<List<Transaction>>

    fun insertOrUpdate(transaction: Transaction): Completable

    fun delete(transaction: Transaction): Completable
}
