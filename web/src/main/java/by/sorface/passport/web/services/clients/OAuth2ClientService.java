package by.sorface.passport.web.services.clients;

import by.sorface.passport.web.dao.models.OAuth2Client;

import java.util.List;
import java.util.UUID;

/**
 * Interface for OAuth2ClientService.
 * This interface provides methods for managing OAuth2Clients.
 */
public interface OAuth2ClientService {

    /**
     * Saves an OAuth2Client.
     *
     * @param oAuth2Client The OAuth2Client to be saved.
     * @return The saved OAuth2Client.
     */
    OAuth2Client save(final OAuth2Client oAuth2Client);

    /**
     * Finds an OAuth2Client by its clientId.
     * @param clientId The clientId of the OAuth2Client to be found.
     * @return The found OAuth2Client.
     */
    OAuth2Client findByClientId(final String clientId);

    /**
     * Finds an OAuth2Client by its id.
     * @param id The id of the OAuth2Client to be found.
     * @return The found OAuth2Client.
     */
    OAuth2Client findById(final UUID id);

    /**
     * Finds all OAuth2Clients by a user's id.
     * @param id The id of the user to find OAuth2Clients for.
     * @return A list of OAuth2Clients for the specified user.
     */
    List<OAuth2Client> findAllByUserId(final UUID id);

    /**
     * Finds an OAuth2Client by its id and user's id.
     * @param id The id of the OAuth2Client to be found.
     * @param userId The id of the user to find the OAuth2Client for.
     * @return The found OAuth2Client.
     */
    OAuth2Client findByIdAndUserId(final UUID id, final UUID userId);

    /**
     * Checks if an OAuth2Client exists by its id.
     * @param id The id of the OAuth2Client to be checked.
     * @return True if the OAuth2Client exists, false otherwise.
     */
    boolean isExists(final UUID id);

    /**
     * Deletes an OAuth2Client by its id.
     * @param id The id of the OAuth2Client to be deleted.
     */
    void delete(final UUID id);

}
