package com.cjhub.data.dao

import io.reactivex.Single

import android.arch.persistence.room.Dao
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.cjhub.domain.contracts.dao.CurrencyDao

import com.cjhub.data.entities.CurrencyEntity

/**
 * Data access object for Currency entity.
 */
@Dao
interface CurrencyDaoImpl : CurrencyDao {

    @Query("SELECT * FROM currencies;")
    fun getAll(): Single<List<CurrencyEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(currency: CurrencyEntity)
}
