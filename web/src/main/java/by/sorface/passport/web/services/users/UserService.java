package by.sorface.passport.web.services.users;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.ProviderType;

import java.util.UUID;

public interface UserService {

    UserEntity findById(final UUID id);

    /**
     * Getting a user by login
     *
     * @param username user's login
     * @return user
     */
    UserEntity findByUsername(final String username);

    /**
     * Receiving a user by email
     *
     * @param email user's email address
     * @return user
     */
    UserEntity findByEmail(final String email);

    /**
     * Receiving a user by login or email
     *
     * @param username user's login
     * @param email    user's email address
     * @return user
     */
    UserEntity findByUsernameOrEmail(final String username, final String email);

    /**
     * Receiving a user by provider or external id
     *
     * @param provider   provider type
     * @param externalId id external system
     * @return user
     */
    UserEntity findByProviderAndExternalId(final ProviderType provider, final String externalId);

    /**
     * Creating a new user
     *
     * @param user user data
     * @return user
     */
    UserEntity save(final UserEntity user);

    /**
     * Проверка существования такого никнейма пользователя
     *
     * @param username проверяемый никнейм
     * @return true - существует, false - нет
     */
    boolean isExistUsername(final String username);
}
