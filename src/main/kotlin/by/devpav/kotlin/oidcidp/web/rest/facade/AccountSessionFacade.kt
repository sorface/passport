package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountContextSession


/**
 * Интерфейс для работы с сессиями аккаунта
 */
interface AccountSessionFacade {

    /**
     * Возвращает действующие сессии аккаунта
     *
     * @return содержащий информацию о действующих сессии аккаунта
     */
    fun getAll(): AccountContextSession

    /**
     * Возвращает действующие сессии аккаунта
     *
     * @return содержащий информацию о действующих сессии аккаунта
     */
    fun getAllByUsername(username: String): AccountContextSession

    /**
     * Удаляет сессии текущего аккаунта
     *
     * @param accountCleanupSessionRequest Объект AccountCleanupSessionRequest, содержащий информацию о сессиях для удаления.
     * @return Объект AccountContextSession, содержащий информацию об удаленных сессиях пользователя
     */
    fun deleteMultiple(accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession

    /**
     * Удаляет сессии аккаунта по имени пользователя
     *
     * @param accountCleanupSessionRequest Объект AccountCleanupSessionRequest, содержащий информацию о сессиях для удаления.
     * @return Объект AccountContextSession, содержащий информацию об удаленных сессиях пользователя
     */
    fun deleteMultipleByUsername(username: String, accountCleanupSessionRequest: AccountCleanupSessionRequest): AccountContextSession
}
