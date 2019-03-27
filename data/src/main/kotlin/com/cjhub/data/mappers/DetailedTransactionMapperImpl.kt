package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Account
import com.cjhub.domain.models.Category
import com.cjhub.domain.models.Transaction

import com.cjhub.data.entities.AccountEntity
import com.cjhub.data.entities.CategoryEntity
import com.cjhub.data.entities.DetailedTransactionEntity

import java.time.Instant
import java.time.ZoneId

/**
 * Mapper between detailed Transaction entity and domain model.
 */
class DetailedTransactionMapperImpl(
    private val categoryMapper: Mapper<CategoryEntity, Category>,
    private val accountMapper: Mapper<AccountEntity, Account>
) : Mapper<DetailedTransactionEntity, Transaction> {

    override fun toModel(from: DetailedTransactionEntity): Transaction {
        val transaction = from.transaction
        val category = from.category
        val sourceAccount = from.sourceAccount
        val destinationAccount = from.destinationAccount

        return Transaction(
            transaction.id,
            Instant.ofEpochMilli(transaction.dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime(),
            accountMapper.toModel(sourceAccount),
            categoryMapper.toModel(category),
            destinationAccount?.let { account ->
                accountMapper.toModel(account)
            } ?: Account.NO_ACCOUNT,
            transaction.amount,
            transaction.description ?: ""
        )
    }

    override fun toEntity(from: Transaction): DetailedTransactionEntity {
        throw NotImplementedError("This operation cannot be implemented!")
    }
}
