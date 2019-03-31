package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Currency

import com.cjhub.data.entities.CurrencyEntity

/**
 * Mapper between Currency entity and domain model.
 */
class CurrencyMapperImpl : Mapper<CurrencyEntity, Currency> {

    override fun toModel(from: CurrencyEntity): Currency {
        return Currency(from.id, from.name, from.symbol, from.isActive)
    }

    override fun toEntity(from: Currency): CurrencyEntity {
        return CurrencyEntity(from.id, from.name, from.symbol, from.isActive, System.currentTimeMillis())
    }
}
