package by.devpav.kotlin.oidcidp.dao.nosql.repository

import by.devpav.kotlin.oidcidp.dao.nosql.model.OAuth2AuthorizationInit
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

interface RedisOAuth2AuthorizationInitRepository : CrudRepository<OAuth2AuthorizationInit?, String?>, QueryByExampleExecutor<OAuth2AuthorizationInit?> {
    fun findFirstByCode(code: String?): OAuth2AuthorizationInit?

    fun findFirstByState(state: String?): OAuth2AuthorizationInit?
}
