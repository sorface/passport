package by.sorface.passport.web.services.redis;

import by.sorface.passport.web.services.redis.events.OAuth2AuthorizationCompleteEvent;
import by.sorface.passport.web.services.redis.events.OAuth2AuthorizationDeleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * The RedisOAuth2AuthorizationDetailsService instance used to save and delete authorization details.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2AuthorizationListener implements OAuth2AuthorizationListener {

    private final RedisOAuth2AuthorizationDetailsService redisOAuth2AuthorizationDetailsService;

    /**
     * Listens for OAuth2AuthorizationCompleteEvent and saves the authorization details.
     * If the save operation is successful, it logs a success message. If not, it logs a warning message.
     *
     * @param event The OAuth2AuthorizationCompleteEvent to handle.
     */
    @Override
    @EventListener(OAuth2AuthorizationCompleteEvent.class)
    public void complete(OAuth2AuthorizationCompleteEvent event) {
        final var authorization = event.authorization();

        if (redisOAuth2AuthorizationDetailsService.save(authorization)) {
            log.info("save authorization complete event with id '{}'", authorization.getId());
        } else {
            log.warn("save authorization complete event with id '{}' failed", authorization.getId());
        }
    }

    /**
     * Listens for OAuth2AuthorizationDeleteEvent and deletes the authorization details.
     *
     * @param event The OAuth2AuthorizationDeleteEvent to handle.
     */
    @Override
    @EventListener(OAuth2AuthorizationDeleteEvent.class)
    public void delete(OAuth2AuthorizationDeleteEvent event) {
        redisOAuth2AuthorizationDetailsService.deleteById(event.authorization().getId());
    }

}
