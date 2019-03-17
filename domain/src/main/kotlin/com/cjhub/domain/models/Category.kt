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
    companion object {
        val OTHER_INCOME = Category(1L, "Other", Type.INCOME, 0.0f)
        val OTHER_EXPENSE = Category(2L, "Other", Type.EXPENSE, 0.0f)
        val TRANSFER = Category(3L, "Transfer", Type.TRANSFER, 0.0f)
        val NO_CATEGORY = Category()
    }
}
