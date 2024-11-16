package by.sorface.passport.web.services.redis

import by.sorface.passport.web.config.options.OAuth2Options
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.lang.Nullable
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class RedisOAuth2AuthorizationConsentService(
    private val redisTemplate: RedisTemplate<String, OAuth2AuthorizationConsent>,
    private val oAuth2Options: OAuth2Options
) : OAuth2AuthorizationConsentService {

    private val authorizationConsents: ValueOperations<String, OAuth2AuthorizationConsent> = redisTemplate.opsForValue()

    override fun save(authorizationConsent: OAuth2AuthorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null")
        val id = getId(authorizationConsent)

        authorizationConsents[buildKey(id), authorizationConsent, oAuth2Options.redis!!.consent!!.ttl] = oAuth2Options.redis!!.consent!!.unit!!
    }

    override fun remove(authorizationConsent: OAuth2AuthorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null")
        val id = getId(authorizationConsent)

        redisTemplate.delete(buildKey(id))
    }

    @Nullable
    override fun findById(registeredClientId: String, principalName: String): OAuth2AuthorizationConsent? {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty")
        Assert.hasText(principalName, "principalName cannot be empty")
        val id = getId(registeredClientId, principalName)

        val ttl = oAuth2Options.redis!!.consent!!.ttl
        val unit = oAuth2Options.redis!!.consent!!.unit

        return authorizationConsents.getAndExpire(id, ttl, unit)
    }

    private fun buildKey(id: String): String = oAuth2Options.redis!!.consent!!.prefix + id

    companion object {

        private fun getId(registeredClientId: String, principalName: String): String = Objects.hash(registeredClientId, principalName).toString()

        private fun getId(authorizationConsent: OAuth2AuthorizationConsent): String = getId(authorizationConsent.registeredClientId, authorizationConsent.principalName)

    }
}
