package by.sorface.passport.web.dao.nosql.redis.repository

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationInit
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisOAuth2AuthorizationInitRepository : CrudRepository<RedisOAuth2AuthorizationInit?, String?>, QueryByExampleExecutor<RedisOAuth2AuthorizationInit?> {
    fun findFirstByCode(code: String?): RedisOAuth2AuthorizationInit?

    fun findFirstByState(state: String?): RedisOAuth2AuthorizationInit?
}
