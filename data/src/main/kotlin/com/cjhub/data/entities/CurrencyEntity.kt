package com.cjhub.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Room-specific entity for Currency domain model.
 */
@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
) {
    companion object {
        val DOLLAR = CurrencyEntity(1L, "USD", "$", true, System.currentTimeMillis())
        val EURO = CurrencyEntity(2L, "EUR", "€", false, System.currentTimeMillis())
        val POUND = CurrencyEntity(3L, "GBP", "£", false, System.currentTimeMillis())
        val BAHT = CurrencyEntity(4L, "THB", "฿", false, System.currentTimeMillis())
    }
}
