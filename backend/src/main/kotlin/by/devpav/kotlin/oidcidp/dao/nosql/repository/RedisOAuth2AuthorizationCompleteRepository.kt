package by.devpav.kotlin.oidcidp.dao.nosql.repository

import by.devpav.kotlin.oidcidp.dao.nosql.model.OAuth2AuthorizationComplete
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

/**
 * Интерфейс RedisOAuth2AuthorizationCompleteRepository представляет собой репозиторий для работы с объектами OAuth2AuthorizationComplete в Redis.
 * Он расширяет интерфейсы CrudRepository и QueryByExampleExecutor для предоставления базовых CRUD-операций и поиска по примеру.
 */
interface RedisOAuth2AuthorizationCompleteRepository : CrudRepository<OAuth2AuthorizationComplete, String>,
    QueryByExampleExecutor<OAuth2AuthorizationComplete> {

    /**
     * Метод findFirstByAccessToken находит первый объект OAuth2AuthorizationComplete по заданному токену доступа.
     * @param accessToken токен доступа, по которому производится поиск.
     * @return объект OAuth2AuthorizationComplete или null, если объект не найден.
     */
    fun findFirstByAccessToken(accessToken: String): OAuth2AuthorizationComplete?

    /**
     * Метод findFirstByRefreshToken находит первый объект OAuth2AuthorizationComplete по заданному токену обновления.
     * @param refreshToken токен обновления, по которому производится поиск.
     * @return объект OAuth2AuthorizationComplete или null, если объект не найден.
     */
    fun findFirstByRefreshToken(refreshToken: String): OAuth2AuthorizationComplete?

    /**
     * Метод findFirstByOidcToken находит первый объект OAuth2AuthorizationComplete по заданному OIDC-токену.
     * @param oidcToken OIDC-токен, по которому производится поиск.
     * @return объект OAuth2AuthorizationComplete или null, если объект не найден.
     */
    fun findFirstByOidcToken(oidcToken: String): OAuth2AuthorizationComplete?

}
