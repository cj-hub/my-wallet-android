package com.cjhub.domain.models

import java.time.LocalDateTime

/**
 * Represents Transaction domain model.
 */
data class Transaction(
    val id: Long = 0L,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val sourceAccount: Account = Account.NO_ACCOUNT,
    val category: Category = Category.NO_CATEGORY,
    val destinationAccount: Account = Account.NO_ACCOUNT,
    val amount: Float = 0.0f,
    val description: String = ""
) {

    fun isValidForCreate(): Boolean {
        return category.type == Type.INCOME || amount <= sourceAccount.balance
    }

    fun isValidForUpdate(old: Transaction): Boolean {
        return if (sourceAccount == old.sourceAccount) {
            copy(sourceAccount = sourceAccount.copy(
                balance = sourceAccount.balance - when (old.category.type) {
                    Type.INCOME -> old.amount
                    Type.EXPENSE, Type.TRANSFER -> -old.amount
                    else -> 0.0f
                }
            )).isValidForCreate()
        } else {
            isValidForCreate()
        }
    }
}
