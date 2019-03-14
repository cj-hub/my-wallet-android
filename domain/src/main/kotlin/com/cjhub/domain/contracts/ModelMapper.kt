package com.cjhub.domain.contracts

/**
 * Mapper used to map data entities to domain models and vice versa.
 */
interface ModelMapper<E, M> {

    fun toModel(from: E): M

    fun toEntity(from: M): E
}
