package by.sorface.passport.web.services.redis;

import by.sorface.passport.web.config.options.OAuth2Options;
import by.sorface.passport.web.dao.nosql.redis.RedisOAuth2AuthorizationCompleteRepository;
import by.sorface.passport.web.dao.nosql.redis.RedisOAuth2AuthorizationInitRepository;
import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete;
import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisOAuth2AuthorizationCompleteRepository redisOAuth2AuthorizationCompleteRepository;

    private final RedisOAuth2AuthorizationInitRepository redisOAuth2AuthorizationInitRepository;

    private static boolean isComplete(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }

    @Override
    public void save(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        log.info("save user's authorization object with id {}", authorization.getId());

        if (isComplete(authorization)) {
            if (Boolean.TRUE.equals(this.redisOAuth2AuthorizationInitRepository.existsById(authorization.getId()))) {
                this.redisOAuth2AuthorizationInitRepository.deleteById(authorization.getId());
            }

            final var authorizationComplete = RedisOAuth2AuthorizationComplete.builder()
                    .id(authorization.getId())
                    .accessToken(getTokenValueOrNull(authorization.getAccessToken()))
                    .refreshToken(getTokenValueOrNull(authorization.getRefreshToken()))
                    .oidcToken(getTokenValueOrNull(authorization.getToken(OidcIdToken.class)))
                    .authorization(authorization)
                    .build();

            log.info("saved user's complete authorization object with id {}", authorization.getId());

            redisOAuth2AuthorizationCompleteRepository.save(authorizationComplete);
        } else {
            final String state = Optional.ofNullable(authorization.getAttribute(OAuth2ParameterNames.STATE))
                    .filter(String.class::isInstance)
                    .map(String::valueOf)
                    .orElse(null);

            final String code = getTokenValueOrNull(authorization.getToken(OAuth2AuthorizationCode.class));

            final var authorizationInit = RedisOAuth2AuthorizationInit.builder()
                    .id(authorization.getId())
                    .code(code)
                    .state(state)
                    .authorization(authorization)
                    .build();

            log.info("saved user's init authorization object with id {}", authorization.getId());

            redisOAuth2AuthorizationInitRepository.save(authorizationInit);
        }
    }

    @Override
    public void remove(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        log.debug("delete authorization object with key {}", authorization.getId());

        redisOAuth2AuthorizationCompleteRepository.deleteById(authorization.getId());
        redisOAuth2AuthorizationInitRepository.deleteById(authorization.getId());
    }

    @Nullable
    @Override
    public OAuth2Authorization findById(final String id) {
        Assert.hasText(id, "id cannot be empty");

        return this.redisOAuth2AuthorizationCompleteRepository.findById(id)
                .map(RedisOAuth2AuthorizationComplete::getAuthorization)
                .or(()-> redisOAuth2AuthorizationInitRepository.findById(id).map(RedisOAuth2AuthorizationInit::getAuthorization))
                .orElse(null);
    }

    @Nullable
    @Override
    public OAuth2Authorization findByToken(final String token, final @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        if (Objects.isNull(tokenType)) {
            return redisOAuth2AuthorizationInitRepository.findFirstByCode(token)
                    .map(RedisOAuth2AuthorizationInit::getAuthorization)
                    .or(() -> redisOAuth2AuthorizationInitRepository.findFirstByState(token).map(RedisOAuth2AuthorizationInit::getAuthorization))
                    .or(() -> redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token).map(RedisOAuth2AuthorizationComplete::getAuthorization))
                    .or(() -> redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token).map(RedisOAuth2AuthorizationComplete::getAuthorization))
                    .or(() -> redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token).map(RedisOAuth2AuthorizationComplete::getAuthorization))
                    .orElse(null);
        }

        if (OAuth2ParameterNames.CODE.equalsIgnoreCase(tokenType.getValue())) {
            return this.redisOAuth2AuthorizationInitRepository.findFirstByCode(token).map(RedisOAuth2AuthorizationInit::getAuthorization).orElse(null);
        }

        if (OAuth2ParameterNames.STATE.equalsIgnoreCase(tokenType.getValue())) {
            return this.redisOAuth2AuthorizationInitRepository.findFirstByState(token).map(RedisOAuth2AuthorizationInit::getAuthorization).orElse(null);
        }

        if (OAuth2ParameterNames.ACCESS_TOKEN.equalsIgnoreCase(tokenType.getValue())) {
            return this.redisOAuth2AuthorizationCompleteRepository.findFirstByAccessToken(token).map(RedisOAuth2AuthorizationComplete::getAuthorization).orElse(null);
        }

        if (OAuth2ParameterNames.REFRESH_TOKEN.equalsIgnoreCase(tokenType.getValue())) {
            return this.redisOAuth2AuthorizationCompleteRepository.findFirstByRefreshToken(token).map(RedisOAuth2AuthorizationComplete::getAuthorization).orElse(null);
        }

        return redisOAuth2AuthorizationCompleteRepository.findFirstByOidcToken(token).map(RedisOAuth2AuthorizationComplete::getAuthorization).orElse(null);
    }

    private <T extends OAuth2Token> String getTokenValueOrNull(OAuth2Authorization.Token<T> oauth2Token) {
        return Optional.ofNullable(oauth2Token)
                .map(OAuth2Authorization.Token::getToken)
                .map(token -> oauth2Token.getToken().getTokenValue())
                .orElse(null);
    }
}
