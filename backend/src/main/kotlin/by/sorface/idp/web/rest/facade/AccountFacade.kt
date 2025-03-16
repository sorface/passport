package by.sorface.idp.web.rest.facade

import by.sorface.idp.web.rest.model.accounts.*
import java.util.*

/**
 * Интерфейс для работы с аккаунтами пользователей
 */
interface AccountFacade {

    /**
     * Проверяет, аутентифицирован ли текущий пользователь
     *
     * @return Объект AccountAuthenticated, содержащий информацию об аутентификации.
     */
    fun isAuthenticated(): AccountAuthenticated

    /**
     * Возвращает профиль текущего авторизованного пользователя.
     *
     * @return Объект ProfileRecord, содержащий информацию о профиле пользователя.
     */
    fun getCurrentAuthorized(): Account

    /**
     * Обновляет информацию об аккаунте пользователя.
     *
     * @param id Идентификатор аккаунта.
     * @param request Объект AccountPatchUpdate, содержащий информацию для обновления.
     * @return Объект ProfileRecord, содержащий обновленную информацию о профиле пользователя.
     */
    fun update(id: UUID, request: AccountPatchUpdate): Account

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя
     *
     * @param username Имя пользователя.
     * @return Объект AccountExistsResponse, содержащий информацию о существовании пользователя.
     */
    fun isExistsByUsername(username: String): AccountExistsResponse

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя или email
     *
     * @param login имя пользователя или email
     * @return Объект AccountExistsResponse, содержащий информацию о существовании пользователя.
     */
    fun isExistsByUsernameOrEmail(login: String): AccountExistsResponse

    /**
     * Обновляет имя пользователя аккаунта.
     *
     * @param id Идентификатор аккаунта.
     * @param request Объект AccountUsernamePatchUpdate, содержащий новое имя пользователя.
     * @return Объект ProfileRecord, содержащий обновленную информацию о профиле пользователя.
     */
    fun updateUsername(id: UUID, request: AccountUsernameUpdate): Account

}