package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import java.util.*

/**
 * Контракт взаимодействия с пользователями системы
 */
interface UserService {

    /**
     * Получение пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     *
     * @return пользователь или null
     */
    fun findById(id: UUID): UserEntity?

    /**
     * Получение пользователя по идентификатору или порождение исключения в случае если пользователь не найден
     *
     * @param id идентификатор пользователя
     * @param throwableConsumer функция обратного вызова для порождения исключения в случае когда пользователь не найден
     *
     * @return пользователь
     */
    fun findByIdOrThrow(id: UUID, throwableConsumer: (id: UUID) -> Throwable): UserEntity

    /**
     * Получение пользователя по идентификационному имени
     *
     * @param username идентификационное имя пользователя
     *
     * @return пользователь или null
     */
    fun findByUsername(username: String): UserEntity?

    /**
     * Получение пользователя по email
     *
     * @param email адрес почты пользователя
     *
     * @return пользователь или null
     */
    fun findByEmail(email: String): UserEntity?

    /**
     * Получение пользователя по идентификационному имени или email
     *
     * @param username идентификационное имя пользователя
     * @param email email пользователя
     *
     * @return пользователь или null
     */
    fun findByUsernameOrEmail(username: String, email: String): UserEntity?

    /**
     * Поиск пользователя по типу провайдера и внешнему идентификатору
     *
     * @param provider тип провайдера
     * @param externalId внешний идентификатор
     *
     * @return пользователь или null
     */
    fun findByProviderAndExternalId(provider: ProviderType, externalId: String): UserEntity?

    /**
     * Сохранения нового пользователя или обновление существующего
     *
     * @param user данные нового/существующего пользователя
     *
     * @return сохраненный пользователь
     */
    fun save(user: UserEntity): UserEntity

    /**
     * Проверка существования пользователя в системе по идентификационному имени
     *
     * @param username идентификационное имя
     *
     * @return true - пользователь с переданным идентификационным именем существует, false - пользователь не существует
     */
    fun isExistUsername(username: String): Boolean

    /**
     * Проверка существования пользователя в системе по email
     *
     * @param email адрес почты пользователя
     *
     * @return true - пользователь с переданным адресом почты существует, false - пользователь не существует
     */
    fun isExistEmail(email: String): Boolean

}
