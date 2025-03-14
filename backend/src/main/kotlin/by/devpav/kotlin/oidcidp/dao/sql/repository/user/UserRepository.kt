package by.devpav.kotlin.oidcidp.dao.sql.repository.user

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.model.enums.ProviderType
import by.devpav.kotlin.oidcidp.dao.sql.repository.BaseRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : BaseRepository<UserModel> {

    /**
     * Поиск пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    fun findFirstByUsernameIgnoreCase(username: String?): UserModel?

    /**
     * Поиск пользователя по элетронной почте
     *
     * @param email элетронная почта
     * @return пользователь
     */
    fun findFirstByEmailIgnoreCase(email: String?): UserModel?

    /**
     * Поиск пользователя по логину или электронной почте
     *
     * @param username логин пользователя
     * @param email    email пользователя
     * @return пользователь
     */
    fun findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username: String?, email: String?): UserModel?

    /**
     * Проверка существования пользователя в БД по логину или электронной почте
     *
     * @param username логин пользователя
     * @param email    email пользователя
     * @return пользователь
     */
    fun existsByUsernameOrEmail(username: String?, email: String?): Boolean

    /**
     * Поиск пользователя по провайдеру и идентификатору внешней системы
     *
     * @param providerType тип провайдера
     * @param externalId   внешний идентификатор клиента
     * @return пользователь
     */
    fun findByProviderTypeAndExternalId(providerType: ProviderType?, externalId: String?): UserModel?

    /**
     * Проверка существования пользователя с таким никнеймом
     *
     * @param username никнейм
     * @return true - уже существует, false - не существует
     */
    fun existsByUsernameIgnoreCase(username: String?): Boolean

    /**
     * Проверка существования пользователя с таким никнеймом
     *
     * @param username никнейм
     * @return true - уже существует, false - не существует
     */
    fun existsByUsername(username: String?): Boolean

    /**
     * Проверка существования пользователя с таким email
     *
     * @param email email
     * @return true - уже существует, false - не существует
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Функция для поиска первого пользователя по имени пользователя.
     *
     * @param username Имя пользователя, по которому будет производиться поиск.
     * @return UserModel? Объект UserModel, если пользователь найден, или null, если пользователь не найден.
     */
    fun findFirstByUsername(username: String): UserModel?

}