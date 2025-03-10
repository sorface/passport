package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.Instant

/**
 * Класс, представляющий временную регистрацию пользователя.
 *
 * @property email адрес электронной почты пользователя
 * @property username имя пользователя
 * @property password пароль пользователя
 * @property otp одноразовый пароль
 * @property firstName имя пользователя
 * @property lastName фамилия пользователя
 */
@RedisHash(value = "passport.registration:tmp", timeToLive = 600)
class TmpRegistration() : BasicRedisEntity() {
    lateinit var email: String
    lateinit var username: String
    lateinit var password: String
    @Indexed
    lateinit var otp: String

    var firstName: String? = null
    var lastName: String? = null
    var otpExpTime: Instant = Instant.now()
}