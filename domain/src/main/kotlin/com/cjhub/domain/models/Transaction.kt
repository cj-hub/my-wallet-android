package com.cjhub.domain.models

import java.util.Calendar
import java.util.Date

/**
 * Represents Transaction domain model.
 */
data class Transaction(
    val id: Long = 0L,
    val date: Date = Calendar.getInstance().time,
    val type: Type = Type.NONE,
    val account: Account = Account(),
    val category: Category = Category(),
    val toAccount: Account = Account(),
    val amount: Float = 0.0f,
    val description: String = ""
)
