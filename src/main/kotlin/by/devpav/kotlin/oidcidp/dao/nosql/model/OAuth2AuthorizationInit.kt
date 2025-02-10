package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

@RedisHash(value = "passport.authorization:init", timeToLive = 300)
class OAuth2AuthorizationInit : BasicRedisEntity() {

    @Indexed
    var code: String? = null

    @Indexed
    var state: String? = null

    var authorization: OAuth2Authorization? = null

}
