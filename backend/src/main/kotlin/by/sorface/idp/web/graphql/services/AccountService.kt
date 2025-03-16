package by.sorface.idp.web.graphql.services

import by.sorface.idp.dao.sql.model.UserModel
import by.sorface.idp.graphql.model.GQAccountUsername
import by.sorface.idp.graphql.model.GQPatchUpdateAccount
import java.util.*

/**
 * Интерфейс сервиса для работы с аккаунтами пользователей.
 */
interface AccountService {

    /**
     * Метод для поиска пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return UserModel или null, если пользователь не найден.
     */
    fun findById(id: UUID): UserModel?

    /**
     * Метод для поиска пользователя по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return UserModel или null, если пользователь не найден.
     */
    fun findByUsername(username: String): UserModel?

    /**
     * Метод для проверки существования пользователя по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return true, если пользователь существует, false в противном случае.
     */
    fun isExistByUsername(username: String): Boolean

    /**
     * Метод для обновления имени пользователя.
     *
     * @param id Идентификатор пользователя.
     * @param username Новое имя пользователя.
     * @return AccountUsername с обновленным именем пользователя.
     */
    fun updateUsername(id: UUID, username: String): GQAccountUsername

    /**
     * Метод для обновления информации о пользователе.
     *
     * @param account Объект PatchUpdateAccount с информацией для обновления.
     * @return UserModel с обновленной информацией или null, если пользователь не найден.
     */
    fun updateInformation(account: GQPatchUpdateAccount): UserModel?

}