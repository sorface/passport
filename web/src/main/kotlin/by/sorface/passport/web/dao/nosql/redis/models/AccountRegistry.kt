package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "passport.account.registry")
class AccountRegistry(
    var email: String,
    var username: String,
    var password: String,
    @Indexed
    var otpId: String,
    @TimeToLive
    val ttlSeconds: Long
) {
    @Id
    var id: String? = null

    var firstName: String? = null

    var lastName: String? = null
}