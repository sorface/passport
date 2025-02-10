package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

@RedisHash("passport.authorization:complete")
class OAuth2AuthorizationComplete : BasicRedisEntity() {

    @Indexed
    var refreshToken: String? = null

    @Indexed
    var oidcToken: String? = null

    @Indexed
    var accessToken: String? = null

    var authorization: OAuth2Authorization? = null

}
