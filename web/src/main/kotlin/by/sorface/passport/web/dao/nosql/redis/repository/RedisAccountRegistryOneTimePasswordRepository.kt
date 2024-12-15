package by.sorface.passport.web.dao.nosql.redis.repository

import by.sorface.passport.web.dao.nosql.redis.models.OneTimePassword
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisAccountRegistryOneTimePasswordRepository : KeyValueRepository<OneTimePassword, String>, QueryByExampleExecutor<OneTimePassword>