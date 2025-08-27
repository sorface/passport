package by.sorface.idp.dao.nosql.listeners

import by.sorface.idp.dao.nosql.model.OAuth2AuthorizationInit
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisKeyExpiredEvent
import org.springframework.stereotype.Component

/**
 * Listens for Redis key expiration events for OAuth2 authorization entities stored with @TimeToLive.
 * Requires Redis keyspace notifications for expirations to be enabled. This is wired by
 * `@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP)` in `RedisConfiguration`.
 */
@Component
class RedisOAuth2AuthorizationListener {

    private val logger = LoggerFactory.getLogger(RedisOAuth2AuthorizationListener::class.java)

    @EventListener
    fun onInitExpired(event: RedisKeyExpiredEvent<OAuth2AuthorizationInit>) {
        logger.info("Redis TTL expired for oauth2 authorization init expired [keyspace -> {}, id -> {}]", event.keyspace, event.id)
    }

}


