package by.sorface.passport.web.dao.sql.repository

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : BaseRepository<UserEntity?> {
    /**
     * Поиск пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    fun findFirstByUsernameIgnoreCase(username: String?): UserEntity?

    /**
     * Поиск пользователя по элетронной почте
     *
     * @param email элетронная почта
     * @return пользователь
     */
    fun findFirstByEmailIgnoreCase(email: String?): UserEntity?

    /**
     * Поиск пользователя по логину или электронной почте
     *
     * @param username логин пользователя
     * @param email    email пользователя
     * @return пользователь
     */
    fun findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username: String?, email: String?): UserEntity?

    /**
     * Поиск пользователя по провайдеру и идентификатору внешней системы
     *
     * @param providerType тип провайдера
     * @param externalId   внешний идентификатор клиента
     * @return пользователь
     */
    fun findByProviderTypeAndExternalId(providerType: ProviderType?, externalId: String?): UserEntity?

    /**
     * Проверка существования пользователя с таким никнеймом
     *
     * @param username никнейм
     * @return true - уже существует, false - не существует
     */
    fun existsByUsernameIgnoreCase(username: String?): Boolean

    /**
     * Проверка существования пользователя с таким email
     *
     * @param email email
     * @return true - уже существует, false - не существует
     */
    fun existsByEmail(email: String): Boolean
}
