package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Transaction

import com.cjhub.data.entities.DetailedTransactionEntity

/**
 * Mapper between detailed Transaction entity and domain model.
 */
class DetailedTransactionMapperImpl : Mapper<DetailedTransactionEntity, Transaction> {

    override fun toModel(from: DetailedTransactionEntity): Transaction = TODO()

    override fun toEntity(from: Transaction): DetailedTransactionEntity = TODO()
}