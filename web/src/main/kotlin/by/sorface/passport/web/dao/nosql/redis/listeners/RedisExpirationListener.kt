package by.sorface.passport.web.dao.nosql.redis.listeners

import by.sorface.passport.web.dao.nosql.redis.models.BasicRedisEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisKeyExpiredEvent
import org.springframework.stereotype.Component

@Component
class RedisExpirationListener<T : BasicRedisEntity> {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RedisExpirationListener::class.java)
    }

    @EventListener
    fun handleRedisKeyExpiredEvent(event: RedisKeyExpiredEvent<T>) =
        log.info("redis value expired [className -> ${event.keyspace}, id -> ${(event.value?.let { (it as BasicRedisEntity).id })}] expired")

}