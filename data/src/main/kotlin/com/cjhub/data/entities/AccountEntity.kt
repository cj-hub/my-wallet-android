package com.cjhub.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Room-specific entity for Account domain model.
 */
@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "balance")
    val balance: Float,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
) {
    companion object {
        val MY_WALLET = AccountEntity(1L, "My Wallet", 0.0f, System.currentTimeMillis())
    }
}
