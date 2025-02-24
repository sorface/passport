package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

/**
 * Класс OAuth2AuthorizationInit представляет собой модель для хранения информации об авторизации OAuth2 в Redis.
 * Экземпляры этого класса хранятся в Redis с временем жизни 300 секунд.
 */
@RedisHash(value = "passport.authorization:init", timeToLive = 300)
class OAuth2AuthorizationInit : BasicRedisEntity() {

    /**
     * Код авторизации, который используется для получения токена доступа.
     * Этот поле индексируется для быстрого поиска.
     */
    @Indexed
    var code: String? = null

    /**
     * Состояние авторизации, которое используется для предотвращения атак повторного воспроизведения.
     * Этот поле индексируется для быстрого поиска.
     */
    @Indexed
    var state: String? = null

    /**
     * Объект OAuth2Authorization, который содержит информацию об авторизации.
     */
    var authorization: OAuth2Authorization? = null

}
