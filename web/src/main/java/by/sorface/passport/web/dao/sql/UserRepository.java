package by.sorface.passport.web.dao.sql;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.ProviderType;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    /**
     * Поиск пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    UserEntity findFirstByUsernameIgnoreCase(final String username);

    /**
     * Поиск пользователя по элетронной почте
     *
     * @param email элетронная почта
     * @return пользователь
     */
    UserEntity findFirstByEmailIgnoreCase(final String email);

    /**
     * Поиск пользователя по логину или электронной почте
     *
     * @param username логин пользователя
     * @param email    email пользователя
     * @return пользователь
     */
    UserEntity findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(final String username, final String email);

    /**
     * Поиск пользователя по провайдеру и идентификатору внешней системы
     *
     * @param providerType тип провайдера
     * @param externalId   внешний идентификатор клиента
     * @return пользователь
     */
    UserEntity findByProviderTypeAndExternalId(final ProviderType providerType, final String externalId);

    /**
     * Проверка существования пользователя с таким никнеймом
     *
     * @param username никнейм
     * @return true - уже существует, false - не существует
     */
    boolean existsByUsernameIgnoreCase(final String username);

}
