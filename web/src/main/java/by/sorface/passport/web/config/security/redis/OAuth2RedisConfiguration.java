package by.sorface.passport.web.config.security.redis;

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete;
import by.sorface.passport.web.dao.nosql.redis.models.converters.OAuth2AuthorizationBytesReadingConverter;
import by.sorface.passport.web.dao.nosql.redis.models.converters.OAuth2AuthorizationBytesWritingConverter;
import by.sorface.passport.web.records.principals.OAuth2Session;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories(basePackages = "by.sorface.passport.web.dao.nosql.redis")
public class OAuth2RedisConfiguration {

    @Bean
    public RedisTemplate<String, RedisOAuth2AuthorizationComplete> redisOAuth2AuthorizationRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final var redisTemplate = new RedisTemplate<String, RedisOAuth2AuthorizationComplete>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplateOAuth2Authorization(final RedisConnectionFactory redisConnectionFactory) {
        return redisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplateOAuth2AuthorizationConsent(final RedisConnectionFactory redisConnectionFactory) {
        return redisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisTemplate<String, OAuth2Session> redisTemplateOAuth2Session(final RedisConnectionFactory redisConnectionFactory) {
        return redisTemplate(redisConnectionFactory);
    }

    private <T> RedisTemplate<String, T> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final var redisTemplate = new RedisTemplate<String, T>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> oauth2redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(OAuth2AuthorizationBytesReadingConverter offsetToBytes,
                                                         OAuth2AuthorizationBytesWritingConverter bytesToOffset) {
        return new RedisCustomConversions(Arrays.asList(offsetToBytes, bytesToOffset));
    }
}
