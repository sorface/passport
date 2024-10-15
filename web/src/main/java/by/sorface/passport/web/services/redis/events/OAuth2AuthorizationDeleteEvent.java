package by.sorface.passport.web.services.redis.events;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

public record OAuth2AuthorizationDeleteEvent(OAuth2Authorization authorization) {
}
