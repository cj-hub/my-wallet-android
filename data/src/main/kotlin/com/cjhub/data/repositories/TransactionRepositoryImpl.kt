package com.cjhub.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.contracts.repositories.TransactionRepository
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction

import com.cjhub.data.dao.TransactionDaoImpl
import com.cjhub.data.entities.DetailedTransactionEntity
import com.cjhub.data.entities.TransactionEntity

/**
 * Room implementation of Transaction repository.
 */
class TransactionRepositoryImpl(
    private val transactionDao: TransactionDaoImpl,
    private val transactionMapper: Mapper<TransactionEntity, Transaction>,
    private val detailedTransactionMapper: Mapper<DetailedTransactionEntity, Transaction>
) : TransactionRepository {

    override fun getAll(): Single<List<Transaction>> {
        return transactionDao.getAll().map { transactions ->
            transactions.map(detailedTransactionMapper::toModel)
        }
    }

    override fun getAllByAccount(account: Account): Single<List<Transaction>> {
        return transactionDao.getAllByAccount(account.id).map { transactions ->
            transactions.map(detailedTransactionMapper::toModel)
        }
    }

    override fun getAllByCategory(category: Category): Single<List<Transaction>> {
        return transactionDao.getAllByCategory(category.id).map { transactions ->
            transactions.map(detailedTransactionMapper::toModel)
        }
    }

    override fun insertOrUpdate(transaction: Transaction): Completable {
        return Completable.fromAction { transactionDao.insertOrUpdate(transactionMapper.toEntity(transaction)) }
    }

    override fun delete(transaction: Transaction): Completable {
        return Completable.fromAction { transactionDao.delete(transactionMapper.toEntity(transaction)) }
    }

    override fun clear(): Completable {
        return Completable.fromAction { transactionDao.clear() }
    }
}
