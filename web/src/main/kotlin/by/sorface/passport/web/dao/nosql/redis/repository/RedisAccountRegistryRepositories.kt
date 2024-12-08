package by.sorface.passport.web.dao.nosql.redis.repository

import by.sorface.passport.web.dao.nosql.redis.models.AccountRegistry
import by.sorface.passport.web.dao.nosql.redis.models.AccountRegistryOTP
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisAccountRegistryRepository : CrudRepository<AccountRegistry, String>, QueryByExampleExecutor<AccountRegistry> {

    fun findByOtpId(id: String): AccountRegistry?

}

interface RedisAccountRegistryOTPRepository : CrudRepository<AccountRegistryOTP, String>, QueryByExampleExecutor<AccountRegistryOTP>