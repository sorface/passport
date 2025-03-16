package by.sorface.idp.web.rest.facade

import by.sorface.idp.web.rest.model.apps.ApplicationClient
import by.sorface.idp.web.rest.model.apps.ApplicationPartialUpdate
import by.sorface.idp.web.rest.model.apps.ApplicationRefreshSecret
import by.sorface.idp.web.rest.model.apps.ApplicationRegistry
import java.util.*

/**
 * Интерфейс для работы с приложениями-клиентами.
 */
interface ApplicationClientFacade {

    /**
     * Возвращает список всех приложений-клиентов, связанных с указанным пользователем.
     *
     * @param userId уникальный идентификатор пользователя
     * @return список приложений-клиентов
     */
    fun findAllByUserId(userId: UUID): List<ApplicationClient>

    /**
     * Возвращает приложение-клиент по его идентификатору и идентификатору пользователя.
     *
     * @param id уникальный идентификатор приложения-клиента
     * @param userId уникальный идентификатор пользователя
     * @return приложение-клиент или null, если приложение не найдено
     */
    fun findByIdAndUserId(id: UUID, userId: UUID): ApplicationClient

    /**
     * Регистрирует новое приложение-клиент.
     *
     * @param registryClient объект, содержащий информацию о приложении-клиенте
     * @return зарегистрированное приложение-клиент или null, если регистрация не удалась
     */
    fun registry(registryClient: ApplicationRegistry): ApplicationClient

    /**
     * Обновляет частичную информацию о приложении-клиенте.
     *
     * @param id уникальный идентификатор приложения-клиента
     * @param request объект, содержащий информацию для обновления
     * @return обновленное приложение-клиент или null, если обновление не удалось
     */
    fun partialUpdateByIdAndUserId(id: UUID, userId: UUID, request: ApplicationPartialUpdate): ApplicationClient

    /**
     * Обновляет секрет приложения-клиента по его идентификатору и идентификатору пользователя.
     *
     * @param id уникальный идентификатор приложения-клиента
     * @param userId уникальный идентификатор пользователя
     * @return обновленный секрет приложения-клиента или null, если обновление не удалось
     */
    fun refreshSecretByIdAndUserId(id: UUID, userId: UUID): ApplicationRefreshSecret

    /**
     * Удаляет приложение-клиент по его идентификатору и идентификатору пользователя.
     *
     * @param clientId уникальный идентификатор приложения-клиента
     */
    fun deleteByIdAndUserId(id: UUID, userId: UUID)

}
