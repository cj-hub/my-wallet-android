package com.cjhub.domain.models

/**
 * Represents Currency domain model.
 */
data class Currency(
    val id: Long = 0L,
    val name: String = "",
    val symbol: String = "",
    val isActive: Boolean = false
)
