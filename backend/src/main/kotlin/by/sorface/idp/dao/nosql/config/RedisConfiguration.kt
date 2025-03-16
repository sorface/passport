package by.sorface.idp.dao.nosql.config

import by.sorface.idp.dao.nosql.converters.OAuth2AuthorizationBytesReadingConverter
import by.sorface.idp.dao.nosql.converters.OAuth2AuthorizationBytesWritingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.convert.RedisCustomConversions
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

/**
 * Конфигурация Redis для приложения.
 */
@Configuration
@EnableRedisRepositories(
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
    basePackages = ["by.sorface.idp.dao.nosql.repository"]
)
class RedisConfiguration {

    /**
     * Создает экземпляр RedisCustomConversions с указанными конвертерами.
     *
     * @param oauth2AuthorizationReadingConverter Конвертер для чтения OAuth2 авторизации.
     * @param oAuth2AuthorizationWritingConverter Конвертер для записи OAuth2 авторизации.
     * @return Экземпляр RedisCustomConversions.
     */
    @Bean
    fun redisCustomConversions(
        oauth2AuthorizationReadingConverter: OAuth2AuthorizationBytesReadingConverter,
        oAuth2AuthorizationWritingConverter: OAuth2AuthorizationBytesWritingConverter
    ): RedisCustomConversions {
        val converters = listOf(oauth2AuthorizationReadingConverter, oAuth2AuthorizationWritingConverter)

        return RedisCustomConversions(converters)
    }

}