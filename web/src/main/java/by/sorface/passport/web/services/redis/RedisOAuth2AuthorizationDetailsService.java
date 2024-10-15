package by.sorface.passport.web.services.redis;

import by.sorface.passport.web.dao.nosql.redis.RedisOAuth2AuthorizationRepository;
import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2Authorization;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.utils.OAuth2AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationDetailsService {

    private final RedisOAuth2AuthorizationRepository redisOAuth2AuthorizationRepository;

    public RedisOAuth2Authorization findById(final String id) {
        return redisOAuth2AuthorizationRepository.findById(id).orElse(null);
    }

    public void deleteById(final String id) {
        redisOAuth2AuthorizationRepository.deleteById(id);
    }

    public List<RedisOAuth2Authorization> findByPrincipleId(final String principleId) {
        final var probe = RedisOAuth2Authorization.builder()
                .principalId(principleId)
                .build();

        final var matcher = ExampleMatcher.matching().withMatcher("principalId", ExampleMatcher.GenericPropertyMatchers.exact());

        final var example = Example.of(probe, matcher);

        final Iterable<RedisOAuth2Authorization> iterable = redisOAuth2AuthorizationRepository.findAll(example);

        return IteratorUtils.toList(iterable.iterator()).stream().filter(Objects::nonNull).toList();
    }

    public boolean save(final OAuth2Authorization authorization) {
        final DefaultPrincipal principal = OAuth2AuthorizationUtils.getPrincipal(authorization);

        if (Objects.isNull(principal)) {
            return false;
        }

        final var redisOAuth2Authorization = new RedisOAuth2Authorization();
        {
            redisOAuth2Authorization.setId(authorization.getId());
            redisOAuth2Authorization.setPrincipalId(principal.getId().toString());
            redisOAuth2Authorization.setAccessValueToken(getTokenValue(authorization.getAccessToken()));
            redisOAuth2Authorization.setRefreshValueToken(getTokenValue(authorization.getRefreshToken()));
            redisOAuth2Authorization.setPrincipalUsername(principal.getUsername());
        }

        redisOAuth2AuthorizationRepository.save(redisOAuth2Authorization);

        return true;
    }

    public List<RedisOAuth2Authorization> findByToken(final String token, final OAuth2TokenType type) {
        final var probe = RedisOAuth2Authorization.builder();

        final var matcher = ExampleMatcher.matching();

        matcher.withMatcher("principalId", ExampleMatcher.GenericPropertyMatchers.exact());

        if (type.equals(OAuth2TokenType.ACCESS_TOKEN)) {
            probe.accessValueToken(token);
            matcher.withMatcher("accessValueToken", ExampleMatcher.GenericPropertyMatchers.exact());
        }

        if (type.equals(OAuth2TokenType.REFRESH_TOKEN)) {
            probe.refreshValueToken(token);
            matcher.withMatcher("refreshValueToken", ExampleMatcher.GenericPropertyMatchers.exact());
        }

        final var example = Example.of(probe.build(), matcher);

        final Iterable<RedisOAuth2Authorization> iterable = redisOAuth2AuthorizationRepository.findAll(example);

        final List<RedisOAuth2Authorization> redisOAuth2Authorizations = IteratorUtils.toList(iterable.iterator());

        if (redisOAuth2Authorizations.isEmpty()) {
            return Collections.emptyList();
        }

        return redisOAuth2Authorizations;
    }

    private AbstractOAuth2Token getToken(final OAuth2Authorization.Token<? extends AbstractOAuth2Token> token) {
        return Optional.ofNullable(token)
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);
    }

    private String getTokenValue(final OAuth2Authorization.Token<? extends AbstractOAuth2Token> token) {
        return Optional.ofNullable(getToken(token))
                .map(AbstractOAuth2Token::getTokenValue)
                .orElse(null);
    }

}
