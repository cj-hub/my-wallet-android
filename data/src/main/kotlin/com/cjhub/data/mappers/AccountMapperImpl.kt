package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Account

import com.cjhub.data.entities.AccountEntity

/**
 * Mapper between Account entity and domain model.
 */
class AccountMapperImpl : Mapper<AccountEntity?, Account> {

    override fun toModel(from: AccountEntity?): Account {
        return from?.let { entity ->
            Account(entity.id, entity.name, entity.balance)
        } ?: Account.NO_ACCOUNT
    }

    override fun toEntity(from: Account): AccountEntity? {
        return when (from) {
            Account.NO_ACCOUNT -> null
            else -> AccountEntity(from.id, from.name, from.balance, System.currentTimeMillis())
        }
    }
}
