package com.cjhub.domain.models

import java.util.Calendar
import java.util.Date

/**
 * Represents Transaction domain model.
 */
data class Transaction(
    val id: Long = 0L,
    val date: Date = Calendar.getInstance().time,
    val account: Account = Account.NO_ACCOUNT,
    val category: Category = Category.NO_CATEGORY,
    val toAccount: Account = Account.NO_ACCOUNT,
    val amount: Float = 0.0f,
    val description: String = ""
)
