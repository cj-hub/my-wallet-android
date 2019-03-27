package com.cjhub.data.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * Room-specific entity for Transaction domain model.
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["cat_id"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["src_acc_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["dest_acc_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "cat_id", index = true)
    val categoryId: Long,
    @ColumnInfo(name = "src_acc_id", index = true)
    val sourceAccountId: Long,
    @ColumnInfo(name = "dest_acc_id", index = true)
    val destinationAccountId: Long?,
    @ColumnInfo(name = "date_time")
    val dateTime: Long,
    @ColumnInfo(name = "amount")
    val amount: Float,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
