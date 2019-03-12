package com.cjhub.domain.models

/**
 * Possible types of a transaction.
 */
enum class Type(val type: String) {
    INCOME("Income"),
    EXPENSE("Expense"),
    TRANSFER("Transfer"),
    NONE("None")
}
