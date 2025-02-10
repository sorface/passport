package by.devpav.kotlin.oidcidp.config

import by.devpav.kotlin.oidcidp.dao.nosql.converters.OAuth2AuthorizationBytesReadingConverter
import by.devpav.kotlin.oidcidp.dao.nosql.converters.OAuth2AuthorizationBytesWritingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.convert.RedisCustomConversions
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@Configuration
@EnableRedisRepositories(
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
    basePackages = ["by.devpav.kotlin.oidcidp.dao.nosql.repository"]
)
class RedisConfiguration {

    @Bean
    fun redisCustomConversions(
        oauth2AuthorizationReadingConverter: OAuth2AuthorizationBytesReadingConverter,
        oAuth2AuthorizationWritingConverter: OAuth2AuthorizationBytesWritingConverter
    ): RedisCustomConversions {
        val converters = listOf(oauth2AuthorizationReadingConverter, oAuth2AuthorizationWritingConverter)

        return RedisCustomConversions(converters)
    }


}