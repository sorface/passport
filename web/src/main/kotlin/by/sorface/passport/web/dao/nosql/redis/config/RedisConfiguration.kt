package by.sorface.passport.web.dao.nosql.redis.config

import by.sorface.passport.web.config.AccountRegistryKeyspace
import by.sorface.passport.web.config.AccountRegistryOneTimePasswordKeyspace
import by.sorface.passport.web.dao.nosql.redis.models.AccountRegistry
import by.sorface.passport.web.dao.nosql.redis.models.OneTimePassword
import by.sorface.passport.web.dao.nosql.redis.models.converters.OAuth2AuthorizationBytesReadingConverter
import by.sorface.passport.web.dao.nosql.redis.models.converters.OAuth2AuthorizationBytesWritingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.convert.KeyspaceConfiguration
import org.springframework.data.redis.core.convert.KeyspaceConfiguration.KeyspaceSettings
import org.springframework.data.redis.core.convert.MappingConfiguration
import org.springframework.data.redis.core.convert.RedisCustomConversions
import org.springframework.data.redis.core.index.IndexConfiguration
import org.springframework.data.redis.core.mapping.RedisMappingContext
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent

@Configuration
@EnableRedisRepositories(
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
    basePackages = ["by.sorface.passport.web.dao.nosql.redis.repository"]
)
class RedisConfiguration {

    @Bean
    fun redisTemplateOAuth2AuthorizationConsent(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, OAuth2AuthorizationConsent> =
        redisTemplate(redisConnectionFactory)

    private fun <T> redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, T> {
        val redisTemplate = RedisTemplate<String, T>()

        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()

        return redisTemplate
    }

    @Bean
    fun oauth2redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()

        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()

        return template
    }

    @Bean
    fun redisCustomConversions(
        oauth2AuthorizationReadingConverter: OAuth2AuthorizationBytesReadingConverter,
        oAuth2AuthorizationWritingConverter: OAuth2AuthorizationBytesWritingConverter
    ): RedisCustomConversions {
        val converters = listOf(oauth2AuthorizationReadingConverter, oAuth2AuthorizationWritingConverter)

        return RedisCustomConversions(converters)
    }

    @Bean
    fun keyValueMappingContext(
        accountRegistryKeyspace: AccountRegistryKeyspace,
        accountRegistryOneTimePasswordKeyspace: AccountRegistryOneTimePasswordKeyspace
    ): RedisMappingContext {
        val keyspaceOneTimePasswordSettings = KeyspaceSettings(OneTimePassword::class.java, accountRegistryOneTimePasswordKeyspace.keyspace)
            .apply {
                setTimeToLive(accountRegistryOneTimePasswordKeyspace.lifeTime.toSeconds())
            }

        val keyspaceAccountRegistrySettings = KeyspaceSettings(AccountRegistry::class.java, accountRegistryKeyspace.keyspace)
            .apply {
                setTimeToLive(accountRegistryKeyspace.lifeTime.toSeconds())
            }

        val keyspaceConfiguration = KeyspaceConfiguration().apply {
            addKeyspaceSettings(keyspaceOneTimePasswordSettings)
            addKeyspaceSettings(keyspaceAccountRegistrySettings)
        }

        return RedisMappingContext(MappingConfiguration(IndexConfiguration(), keyspaceConfiguration))
    }

}
