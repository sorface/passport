package by.sorface.passport.web.dao.nosql.redis

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import java.util.*

interface RedisOAuth2AuthorizationCompleteRepository : CrudRepository<RedisOAuth2AuthorizationComplete, String>,
    QueryByExampleExecutor<RedisOAuth2AuthorizationComplete> {

    fun findFirstByAccessToken(accessToken: String): RedisOAuth2AuthorizationComplete?

    fun findFirstByRefreshToken(refreshToken: String): RedisOAuth2AuthorizationComplete?

    fun findFirstByOidcToken(oidcToken: String): RedisOAuth2AuthorizationComplete?

}
