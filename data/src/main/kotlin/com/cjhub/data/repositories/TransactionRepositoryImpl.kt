package com.cjhub.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.contracts.dao.TransactionDao
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction

import com.cjhub.data.entities.DetailedTransactionEntity
import com.cjhub.data.entities.TransactionEntity

/**
 * Room implementation of Transaction repository.
 */
class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val transactionMapper: Mapper<TransactionEntity, Transaction>,
    private val detailedTransactionMapper: Mapper<DetailedTransactionEntity, Transaction>
) : TransactionRepository {

    override fun getAll(): Single<List<Transaction>> = TODO()

    override fun getAllByAccount(account: Account): Single<List<Transaction>> = TODO()

    override fun getAllByCategory(category: Category): Single<List<Transaction>> = TODO()

    override fun insertOrUpdate(transaction: Transaction): Completable = TODO()

    override fun delete(transaction: Transaction): Completable = TODO()

    override fun clear(): Completable = TODO()
}
