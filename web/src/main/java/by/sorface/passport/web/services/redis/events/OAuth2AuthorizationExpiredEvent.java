package by.sorface.passport.web.services.redis.events;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

public record OAuth2AuthorizationExpiredEvent(OAuth2Authorization authorization) {
}
