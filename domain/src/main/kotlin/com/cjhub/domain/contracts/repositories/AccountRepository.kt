package com.cjhub.domain.contracts.repositories

import io.reactivex.Completable
import io.reactivex.Single

import com.cjhub.domain.models.Account

/**
 * Account repository interface for data layer.
 */
interface AccountRepository {

    fun getAll(): Single<List<Account>>

    fun insertOrUpdate(account: Account): Completable

    fun delete(account: Account): Completable
}
