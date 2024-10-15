package by.sorface.passport.web.services.redis;

import by.sorface.passport.web.config.options.OAuth2Options;
import by.sorface.passport.web.services.redis.events.OAuth2AuthorizationCompleteEvent;
import by.sorface.passport.web.services.redis.events.OAuth2AuthorizationDeleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static by.sorface.passport.web.utils.OAuth2AuthorizationUtils.hasToken;

@Slf4j
@Service
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisTemplate<String, OAuth2Authorization> redisTemplate;

    private final ValueOperations<String, OAuth2Authorization> authorizations;

    private final OAuth2Options oAuth2Options;

    private final ApplicationEventPublisher applicationEventPublisher;

    public RedisOAuth2AuthorizationService(final RedisTemplate<String, OAuth2Authorization> redisTemplate,
                                           final OAuth2Options oAuth2Options,
                                           final ApplicationEventPublisher applicationEventPublisher) {
        this.redisTemplate = redisTemplate;
        this.authorizations = redisTemplate.opsForValue();
        this.oAuth2Options = oAuth2Options;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private static boolean isComplete(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }

    @Override
    public void save(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        log.info("save user's authorization object with id {}", authorization.getId());

        String key;

        if (isComplete(authorization)) {
            key = toComplete(authorization.getId());

            applicationEventPublisher.publishEvent(new OAuth2AuthorizationCompleteEvent(authorization));

            final String initKey = toInit(authorization.getId());

            if (Boolean.TRUE.equals(this.redisTemplate.hasKey(initKey))) {
                redisTemplate.delete(initKey);
            }

            log.info("saved user's complete authorization object with id {}", authorization.getId());
            this.authorizations.set(key, authorization);
        } else {
            key = toInit(authorization.getId());

            log.info("saved user's init authorization object with id {}", authorization.getId());
            this.authorizations.set(key, authorization);
        }
    }

    @Override
    public void remove(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        String key;

        if (isComplete(authorization)) {
            applicationEventPublisher.publishEvent(new OAuth2AuthorizationDeleteEvent(authorization));

            key = toComplete(authorization.getId());
        } else {
            key = toInit(authorization.getId());
        }

        log.debug("delete authorization object with key {}", key);

        this.redisTemplate.delete(key);
    }

    @Nullable
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");

        OAuth2Authorization oAuth2Authorization = this.findById(id, oAuth2Options.getRedis().getComplete());

        if (Objects.isNull(oAuth2Authorization)) {
            oAuth2Authorization = this.findById(id, oAuth2Options.getRedis().getInit());
        }

        return oAuth2Authorization;
    }

    private OAuth2Authorization findById(final String id, final OAuth2Options.RedisDescriptionOptions redisDescriptionOptions) {
        final var finalId = redisDescriptionOptions.getPrefix() + id;

        log.info("find authorization object with key {}", finalId);

        OAuth2Authorization authorization = this.authorizations.get(finalId);

        if (Objects.isNull(authorization)) {
            log.info("not found authorization object with key {}", finalId);
        } else {
            log.info("found authorization object with key {}", finalId);
        }

        return authorization;
    }

    @Nullable
    @Override
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        final var oAuth2Authorization = this.findByToken(token, tokenType, oAuth2Options.getRedis().getComplete());

        return Optional.ofNullable(oAuth2Authorization)
                .orElseGet(() -> this.findByToken(token, tokenType, oAuth2Options.getRedis().getInit()));
    }

    private OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType, final OAuth2Options.RedisDescriptionOptions redisDescriptionOptions) {
        final var keys = redisTemplate.keys(redisDescriptionOptions.getPrefix() + "*");

        final var optionKeys = Optional.ofNullable(keys).orElse(Set.of());

        return optionKeys.stream()
                .map(this.authorizations::get)
                .filter(authorization -> hasToken(authorization, token, tokenType))
                .findFirst()
                .orElse(null);
    }

    private String toComplete(final String id) {
        return oAuth2Options.getRedis().getComplete().getPrefix() + id;
    }

    private String toInit(final String id) {
        return oAuth2Options.getRedis().getInit().getPrefix() + id;
    }

}
