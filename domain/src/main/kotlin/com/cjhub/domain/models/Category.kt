package com.cjhub.domain.models

/**
 * Represents Category domain model.
 */
data class Category(
    val id: Long = 0L,
    val name: String = "",
    val type: Type = Type.NONE,
    val total: Float = 0.0f
)
