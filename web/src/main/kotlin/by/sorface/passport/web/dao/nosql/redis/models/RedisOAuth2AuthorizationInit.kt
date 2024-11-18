package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

@RedisHash(value = "sorface.authorization:init", timeToLive = 300)
class RedisOAuth2AuthorizationInit {
    @Id
    var id: String? = null

    @Indexed
    var code: String? = null

    @Indexed
    var state: String? = null

    var authorization: OAuth2Authorization? = null
}
