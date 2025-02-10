package by.devpav.kotlin.oidcidp.graphql.services

import by.devpav.kotlin.oidcidp.graphql.Session

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
    fun getAllByUsername(username: String) : List<Session>

}