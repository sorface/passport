package by.sorface.passport.web.services.tokens;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;

/**
 * This interface defines the methods for the TokenService.
 */
public interface TokenService {

    /**
     * Finds a token entity by its hash.
     *
     * @param hash The hash of the token to find.
     * @return The token entity if found, otherwise null.
     */
    TokenEntity findByHash(final String hash);

    /**
     * Saves a token for a user with a specific operation type.
     *
     * @param user          The user entity for which the token is saved.
     * @param operationType The operation type of the token.
     * @return The saved token entity.
     */
    TokenEntity saveForUser(final UserEntity user, final TokenOperationType operationType);

    /**
     * Finds a token entity by its email.
     *
     * @param email The email of the token to find.
     * @return The token entity if found, otherwise null.
     */
    TokenEntity findByEmail(final String email);

    /**
     * Deletes a token entity by its hash.
     *
     * @param hash The hash of the token to delete.
     */
    void deleteByHash(final String hash);

}
