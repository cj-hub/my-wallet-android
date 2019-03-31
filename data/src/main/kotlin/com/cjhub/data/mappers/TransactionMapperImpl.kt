package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Transaction

import com.cjhub.data.entities.TransactionEntity

import java.time.ZoneId

/**
 * Mapper between Transaction entity and domain model.
 */
class TransactionMapperImpl : Mapper<TransactionEntity, Transaction> {

    override fun toModel(from: TransactionEntity): Transaction {
        throw  NotImplementedError("This operation cannot be implemented!")
    }

    override fun toEntity(from: Transaction): TransactionEntity {
        return TransactionEntity(
            from.id,
            from.category.id,
            from.sourceAccount.id,
            when (from.destinationAccount) {
                Account.NO_ACCOUNT -> null
                else -> from.destinationAccount.id
            },
            from.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            from.amount,
            if (from.description.isBlank()) {
                null
            } else {
                from.description
            },
            System.currentTimeMillis()
        )
    }
}
