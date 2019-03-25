package com.cjhub.domain.models

/**
 * Represents Currency domain model.
 */
data class Currency(
    val id: Long = 0L,
    val name: String = "",
    val symbol: String = "",
    val isActive: Boolean = false
) {
    companion object {
        val DOLLAR = Currency(1L, "USD", "$", true)
        val EURO = Currency(2L, "EUR", "€", false)
        val POUND = Currency(3L, "GBP", "£", false)
        val BAHT = Currency(4L, "THB", "฿", false)
    }
}
