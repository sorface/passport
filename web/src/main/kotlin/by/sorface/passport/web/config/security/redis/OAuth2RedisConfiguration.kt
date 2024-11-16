package by.sorface.passport.web.config.security.redis

import by.sorface.passport.web.dao.nosql.redis.models.converters.OAuth2AuthorizationBytesReadingConverter
import by.sorface.passport.web.dao.nosql.redis.models.converters.OAuth2AuthorizationBytesWritingConverter
import by.sorface.passport.web.records.principals.OAuth2Session
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.convert.RedisCustomConversions
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import java.util.*

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories(basePackages = ["by.sorface.passport.web.dao.nosql.redis"])
open class OAuth2RedisConfiguration {

    @Bean
    open fun redisTemplateOAuth2AuthorizationConsent(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, OAuth2AuthorizationConsent> {
        return redisTemplate(redisConnectionFactory)
    }

    @Bean
    open fun redisTemplateOAuth2Session(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, OAuth2Session> {
        return redisTemplate(redisConnectionFactory)
    }

    private fun <T> redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, T> {
        val redisTemplate = RedisTemplate<String, T>()

        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()

        return redisTemplate
    }

    @Bean
    open fun oauth2redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()

        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()

        return template
    }

    @Bean
    open fun redisCustomConversions(
        offsetToBytes: OAuth2AuthorizationBytesReadingConverter,
        bytesToOffset: OAuth2AuthorizationBytesWritingConverter
    ): RedisCustomConversions {
        return RedisCustomConversions(listOf(offsetToBytes, bytesToOffset))
    }
}
