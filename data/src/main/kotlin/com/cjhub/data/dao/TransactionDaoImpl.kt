package com.cjhub.data.dao

import io.reactivex.Single

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.cjhub.domain.contracts.dao.TransactionDao

import com.cjhub.data.entities.DetailedTransactionEntity
import com.cjhub.data.entities.TransactionEntity

/**
 * Data access object for Transaction entity.
 */
@Dao
interface TransactionDaoImpl : TransactionDao {

    @Query("SELECT t.*, "
            + "c.id AS category_id, c.name AS category_name, c.type AS category_type, c.total AS category_total, c.timestamp AS category_timestamp, "
            + "sa.id AS source_account_id, sa.name AS source_account_name, sa.balance AS source_account_balance, sa.timestamp AS source_account_timestamp, "
            + "da.id AS destination_account_id, da.name AS destination_account_name, da.balance AS destination_account_balance, da.timestamp AS destination_account_timestamp "
            + "FROM transactions AS t "
            + "JOIN categories AS c ON t.cat_id = c.id "
            + "JOIN accounts AS sa ON t.src_acc_id = sa.id "
            + "JOIN accounts AS da ON t.dest_acc_id = da.id;"
    )
    fun getAll(): Single<List<DetailedTransactionEntity>>

    @Query("SELECT t.*, "
            + "c.id AS category_id, c.name AS category_name, c.type AS category_type, c.total AS category_total, c.timestamp AS category_timestamp, "
            + "sa.id AS source_account_id, sa.name AS source_account_name, sa.balance AS source_account_balance, sa.timestamp AS source_account_timestamp, "
            + "da.id AS destination_account_id, da.name AS destination_account_name, da.balance AS destination_account_balance, da.timestamp AS destination_account_timestamp "
            + "FROM transactions AS t "
            + "JOIN categories AS c ON t.cat_id = c.id "
            + "JOIN accounts AS sa ON t.src_acc_id = sa.id "
            + "JOIN accounts AS da ON t.dest_acc_id = da.id "
            + "WHERE t.src_acc_id = :accountId "
            + "OR t.dest_acc_id = :accountId;"
    )
    fun getAllByAccount(accountId: Long): Single<List<DetailedTransactionEntity>>

    @Query("SELECT t.*, "
            + "c.id AS category_id, c.name AS category_name, c.type AS category_type, c.total AS category_total, c.timestamp AS category_timestamp, "
            + "sa.id AS source_account_id, sa.name AS source_account_name, sa.balance AS source_account_balance, sa.timestamp AS source_account_timestamp, "
            + "da.id AS destination_account_id, da.name AS destination_account_name, da.balance AS destination_account_balance, da.timestamp AS destination_account_timestamp "
            + "FROM transactions AS t "
            + "JOIN categories AS c ON t.cat_id = c.id "
            + "JOIN accounts AS sa ON t.src_acc_id = sa.id "
            + "JOIN accounts AS da ON t.dest_acc_id = da.id "
            + "WHERE t.cat_id = :categoryId;")
    fun getAllByCategory(categoryId: Long): Single<List<DetailedTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(transaction: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions;")
    fun clear()
}
