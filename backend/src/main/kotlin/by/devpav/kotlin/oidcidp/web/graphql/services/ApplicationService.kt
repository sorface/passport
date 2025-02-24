package by.devpav.kotlin.oidcidp.web.graphql.services

import by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel
import java.util.UUID

/**
 * Интерфейс сервиса для работы с приложениями.
 */
interface ApplicationService {

    /**
     * Метод для получения всех зарегистрированных клиентов.
     *
     * @return Список зарегистрированных клиентов.
     */
    fun getAll() : List<RegisteredClientModel>

    /**
     * Метод для получения всех зарегистрированных клиентов по идентификатору пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Список зарегистрированных клиентов, связанных с пользователем.
     */
    fun getAllByUser(id: UUID) : List<RegisteredClientModel>

}