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

    override fun getAll(): Single<List<Account>> = TODO()

    override fun insertOrUpdate(account: Account): Completable = TODO()

    override fun delete(account: Account): Completable = TODO()

    override fun reset(): Completable = TODO()
}
