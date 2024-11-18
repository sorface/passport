package by.sorface.passport.web.dao.nosql.redis.repository

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisOAuth2AuthorizationCompleteRepository : CrudRepository<RedisOAuth2AuthorizationComplete, String>,
    QueryByExampleExecutor<RedisOAuth2AuthorizationComplete> {

    fun findFirstByAccessToken(accessToken: String): RedisOAuth2AuthorizationComplete?

    fun findFirstByRefreshToken(refreshToken: String): RedisOAuth2AuthorizationComplete?

    fun findFirstByOidcToken(oidcToken: String): RedisOAuth2AuthorizationComplete?

}
