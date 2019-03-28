package com.cjhub.data.mappers

import com.cjhub.domain.contracts.Mapper
import com.cjhub.domain.models.Account

import com.cjhub.data.entities.AccountEntity

/**
 * Mapper between Account entity and domain model.
 */
class AccountMapperImpl : Mapper<AccountEntity?, Account> {

    override fun toModel(from: AccountEntity?): Account = TODO()

    override fun toEntity(from: Account): AccountEntity = TODO()
}
