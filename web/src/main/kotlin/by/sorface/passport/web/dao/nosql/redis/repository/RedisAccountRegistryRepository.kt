package by.sorface.passport.web.dao.nosql.redis.repository

import by.sorface.passport.web.dao.nosql.redis.models.AccountRegistry
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisAccountRegistryRepository : KeyValueRepository<AccountRegistry, String>, QueryByExampleExecutor<AccountRegistry>