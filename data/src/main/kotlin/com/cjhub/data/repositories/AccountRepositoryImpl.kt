package com.cjhub.data.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.contracts.repositories.AccountRepository
import com.cjhub.domain.models.Account

import com.cjhub.data.dao.AccountDaoImpl
import com.cjhub.data.entities.AccountEntity

/**
 * Room implementation of Account repository.
 */
class AccountRepositoryImpl(
    private val accountDao: AccountDaoImpl,
    private val accountMapper: Mapper<AccountEntity?, Account>
) : AccountRepository {

    override fun getAll(): Single<List<Account>> {
        return accountDao.getAll().map { accounts -> accounts.map(accountMapper::toModel) }
    }

    override fun insertOrUpdate(account: Account): Completable {
        return Completable.fromAction {
            accountMapper.toEntity(account)?.let { accountEntity ->
                accountDao.insertOfUpdate(accountEntity)
            }
        }
    }

    override fun delete(account: Account): Completable {
        return Completable.fromAction {
            accountMapper.toEntity(account)?.let { accountEntity ->
                accountDao.delete(accountEntity)
            }
        }
    }

    override fun reset(): Completable {
        return Completable.fromAction { accountDao.reset() }
    }
}
