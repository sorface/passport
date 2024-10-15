package by.sorface.passport.web.services.redis;

import by.sorface.passport.web.services.redis.events.OAuth2AuthorizationCompleteEvent;
import by.sorface.passport.web.services.redis.events.OAuth2AuthorizationDeleteEvent;

/**
 * Interface for OAuth2AuthorizationListener.
 * This interface defines two methods: complete and delete.
 * The complete method is called when an OAuth2AuthorizationCompleteEvent occurs.
 * The delete method is called when an OAuth2AuthorizationDeleteEvent occurs.
 */
public interface OAuth2AuthorizationListener {

    /**
     * This method is called when an OAuth2AuthorizationCompleteEvent occurs.
     *
     * @param event The OAuth2AuthorizationCompleteEvent that occurred.
     */
    void complete(final OAuth2AuthorizationCompleteEvent event);

    /**
     * This method is called when an OAuth2AuthorizationDeleteEvent occurs.
     * @param event The OAuth2AuthorizationDeleteEvent that occurred.
     */
    void delete(final OAuth2AuthorizationDeleteEvent event);

}
