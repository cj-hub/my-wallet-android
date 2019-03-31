package com.cjhub.domain.models

/**
 * Represents Account domain model.
 */
data class Account(
    val id: Long = 0L,
    val name: String = "",
    val balance: Float = 0.0f
) {
    companion object {
        val NO_ACCOUNT = Account()
    }

    fun isValidForDelete(relatedTransactions: List<Transaction>): Boolean {
        return relatedTransactions.filter { transaction ->
                    transaction.category.type == Type.TRANSFER
                }
                .groupBy({ it.destinationAccount }, { it.amount })
                .map { (account, amounts) ->
                    account.copy(balance = account.balance - amounts.sum())
                }
                .all { account -> account.balance >= 0.0f }
    }
}
