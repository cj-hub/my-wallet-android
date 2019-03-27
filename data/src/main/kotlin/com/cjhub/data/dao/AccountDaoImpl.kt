package com.cjhub.data.dao

import io.reactivex.Single

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.cjhub.domain.contracts.dao.AccountDao

import com.cjhub.data.entities.AccountEntity

/**
 * Data access object for Account entity.
 */
@Dao
interface AccountDaoImpl : AccountDao {

    @Query("SELECT * FROM accounts;")
    fun getAll(): Single<List<AccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOfUpdate(account: AccountEntity)

    @Delete
    fun delete(account: AccountEntity)

    @Query("UPDATE accounts SET balance = 0.0;")
    fun reset()
}
