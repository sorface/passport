package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.redis.core.index.Indexed

/**
 * Данные регистрации нового пользователя
 */
open class AccountRegistry(var email: String, var username: String, var password: String, @Indexed var otpId: String) : BasicRedisEntity() {

    /**
     * Имя
     */
    var firstName: String? = null

    /**
     * Фамилия
     */
    var lastName: String? = null

}