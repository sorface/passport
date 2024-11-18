package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

@RedisHash("sorface.authorization:complete")
class RedisOAuth2AuthorizationComplete {
    @Id
    var id: String? = null

    @Indexed
    var refreshToken: String? = null

    @Indexed
    var oidcToken: String? = null

    @Indexed
    var accessToken: String? = null

    var authorization: OAuth2Authorization? = null
}
