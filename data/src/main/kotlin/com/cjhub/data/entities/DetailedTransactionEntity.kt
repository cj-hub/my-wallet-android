package com.cjhub.data.entities

import android.arch.persistence.room.Embedded

/**
 * Room-specific entity for Transaction domain model with detailed information.
 */
data class DetailedTransactionEntity(
    @Embedded
    val transaction: TransactionEntity,
    @Embedded
    val category: CategoryEntity,
    @Embedded
    val sourceAccount: AccountEntity,
    @Embedded
    val destinationAccount: AccountEntity
)
