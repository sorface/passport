package by.sorface.passport.web.dao.nosql.redis.repository

import by.sorface.passport.web.dao.nosql.redis.models.OAuth2AuthorizationComplete
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisOAuth2AuthorizationCompleteRepository : CrudRepository<OAuth2AuthorizationComplete, String>,
    QueryByExampleExecutor<OAuth2AuthorizationComplete> {

    fun findFirstByAccessToken(accessToken: String): OAuth2AuthorizationComplete?

    fun findFirstByRefreshToken(refreshToken: String): OAuth2AuthorizationComplete?

    fun findFirstByOidcToken(oidcToken: String): OAuth2AuthorizationComplete?

}
