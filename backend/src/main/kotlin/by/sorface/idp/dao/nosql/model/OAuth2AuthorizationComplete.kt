package by.sorface.idp.dao.nosql.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

/**
 * Класс, представляющий полную авторизацию OAuth2.
 */
@RedisHash("passport.authorization:complete")
class OAuth2AuthorizationComplete : BasicRedisEntity() {

    /**
     * Индексированный токен обновления.
     */
    @Indexed
    var refreshToken: String? = null

    /**
     * Индексированный токен OIDC.
     */
    @Indexed
    var oidcToken: String? = null

    /**
     * Индексированный токен доступа.
     */
    @Indexed
    var accessToken: String? = null

    /**
     * Имя пользователя
     */
    @Indexed
    var principalName: String? = null

    /**
     * Объект авторизации OAuth2.
     */
    var authorization: OAuth2Authorization? = null

}
