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

    fun isValidForUpdate(newCategory: Category, relatedTransactions: List<Transaction>): Boolean {
        return when (type) {
            Type.EXPENSE, Type.TRANSFER -> true
            else -> when (newCategory.type) {
                Type.EXPENSE -> {
                    relatedTransactions.groupBy({ it.sourceAccount }, { it.amount })
                            .map { (account, amounts) ->
                                account.copy(balance = account.balance - 2 * amounts.sum())
                            }
                            .all { account -> account.balance >= 0.0f }
                }
                else -> true
            }
        }
    }
}
