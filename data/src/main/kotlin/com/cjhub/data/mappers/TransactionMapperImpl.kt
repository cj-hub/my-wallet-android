package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Transaction

import com.cjhub.data.entities.TransactionEntity

/**
 * Mapper between Transaction entity and domain model.
 */
class TransactionMapperImpl : Mapper<TransactionEntity, Transaction> {

    override fun toModel(from: TransactionEntity): Transaction = TODO()

    override fun toEntity(from: Transaction): TransactionEntity = TODO()
}
