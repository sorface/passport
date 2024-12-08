package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization

@RedisHash(value = "passport.authorization:init", timeToLive = 300)
class OAuth2AuthorizationInit {

    @Id
    lateinit var id: String

    @Indexed
    var code: String? = null

    @Indexed
    var state: String? = null

    var authorization: OAuth2Authorization? = null
}
