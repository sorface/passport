package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

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
class TmpRegistration(var email: String, var username: String, var password: String, @Indexed var otp: String) : BasicRedisEntity() {
    var firstName: String? = null
    var lastName: String? = null
}