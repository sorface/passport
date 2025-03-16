package by.sorface.idp.web.graphql.services

import by.sorface.idp.graphql.model.GQSession

/**
 * Интерфейс сервиса для работы с сессиями.
 */
interface SessionService {

    /**
     * Метод для получения всех сессий по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Список сессий пользователя.
     */
    fun getAllByUsername(username: String): List<GQSession>

}