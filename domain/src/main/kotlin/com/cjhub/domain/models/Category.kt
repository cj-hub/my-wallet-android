package com.cjhub.domain.models

/**
 * Represents Category domain model.
 */
data class Category(
    val id: Long = 0L,
    val name: String = "",
    val type: Type = Type.NONE,
    val total: Float = 0.0f
) {
    /**
     * Possible types of Category domain model.
     */
    enum class Type(val type: String) {
        INCOME("Income"),
        EXPENSE("Expense"),
        TRANSFER("Transfer"),
        NONE("None")
    }
}
