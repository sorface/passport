package by.sorface.idp.web.graphql.services

import by.sorface.idp.dao.sql.model.client.RegisteredClientModel
import java.util.*

/**
 * Интерфейс сервиса для работы с приложениями.
 */
interface ApplicationService {

    /**
     * Метод для получения всех зарегистрированных клиентов.
     *
     * @return Список зарегистрированных клиентов.
     */
    fun getAll(): List<RegisteredClientModel>

    /**
     * Метод для получения всех зарегистрированных клиентов по идентификатору пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Список зарегистрированных клиентов, связанных с пользователем.
     */
    fun getAllByUser(id: UUID): List<RegisteredClientModel>

}