package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.Instant

@RedisHash(value = "passport.account.registry.otp")
class AccountRegistryOTP(
    val code: String,
    val expiredTime: Instant = Instant.now(),
    @TimeToLive
    val ttlSeconds: Long
) {

    @Id
    var id: String? = null

}