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
}
