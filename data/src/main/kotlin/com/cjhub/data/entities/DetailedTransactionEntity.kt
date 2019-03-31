package com.cjhub.data.entities

import android.arch.persistence.room.Embedded

/**
 * Room-specific entity for Transaction domain model with detailed information.
 */
data class DetailedTransactionEntity(
    @Embedded
    val transaction: TransactionEntity,
    @Embedded(prefix = "category_")
    val category: CategoryEntity,
    @Embedded(prefix = "source_account_")
    val sourceAccount: AccountEntity,
    @Embedded(prefix = "destination_account_")
    val destinationAccount: AccountEntity?
)
