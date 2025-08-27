package by.sorface.idp.dao.nosql.repository

import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationInit
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

/**
 * Интерфейс RedisOAuth2AuthorizationInitRepository представляет собой репозиторий для работы с объектами OAuth2AuthorizationInit в Redis.
 * Он расширяет интерфейсы CrudRepository и QueryByExampleExecutor для предоставления базовых CRUD-операций и поиска по примеру.
 */
interface RedisOAuth2AuthorizationInitRepository : CrudRepository<OAuth2AuthorizationInit, String>,
    QueryByExampleExecutor<OAuth2AuthorizationInit?> {

    /**
     * Метод findFirstByCode находит первый объект OAuth2AuthorizationInit по заданному коду авторизации.
     * @param code код авторизации, по которому производится поиск.
     * @return объект OAuth2AuthorizationInit или null, если объект не найден.
     */
    fun findFirstByCode(code: String?): OAuth2AuthorizationInit?

    /**
     * Метод findFirstByState находит первый объект OAuth2AuthorizationInit по заданному состоянию авторизации.
     * @param state состояние авторизации, по которому производится поиск.
     * @return объект OAuth2AuthorizationInit или null, если объект не найден.
     */
    fun findFirstByState(state: String?): OAuth2AuthorizationInit?

}
