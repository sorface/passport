package by.sorface.passport.web.services.tokens

import by.sorface.passport.web.dao.models.TokenEntity
import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.dao.models.enums.TokenOperationType

/**
 * This interface defines the methods for the TokenService.
 */
interface TokenService {
    /**
     * Finds a token entity by its hash.
     *
     * @param hash The hash of the token to find.
     * @return The token entity if found, otherwise null.
     */
    fun findByHash(hash: String?): TokenEntity?

    /**
     * Saves a token for a user with a specific operation type.
     *
     * @param user          The user entity for which the token is saved.
     * @param operationType The operation type of the token.
     * @return The saved token entity.
     */
    fun saveForUser(user: UserEntity?, operationType: TokenOperationType?): TokenEntity

    /**
     * Finds a token entity by its email.
     *
     * @param email The email of the token to find.
     * @return The token entity if found, otherwise null.
     */
    fun findByEmail(email: String?): TokenEntity?

    /**
     * Deletes a token entity by its hash.
     *
     * @param hash The hash of the token to delete.
     */
    fun deleteByHash(hash: String?)
}
