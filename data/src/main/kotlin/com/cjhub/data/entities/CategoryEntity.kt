package com.cjhub.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Room-specific entity for Category domain model.
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "total")
    val total: Float,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
) {
    companion object {
        val OTHER_INCOME = CategoryEntity(1L, "Other", "Income", 0.0f, System.currentTimeMillis())
        val OTHER_EXPENSE = CategoryEntity(2L, "Other", "Expense", 0.0f, System.currentTimeMillis())
        val TRANSFER = CategoryEntity(3L, "Transfer", "Transfer", 0.0f, System.currentTimeMillis())
    }
}
