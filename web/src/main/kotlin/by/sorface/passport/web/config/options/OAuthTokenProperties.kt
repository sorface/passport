package by.sorface.passport.web.config.options

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.temporal.ChronoUnit

@ConfigurationProperties("passport.oauth.token")
data class OAuthTokenProperties(
    val access: TokenSetting = TokenSetting(),
    val refresh: TokenSetting = TokenSetting(),
    val authorizationCode: TokenSetting = TokenSetting()
) {
    data class TokenSetting(var ttl: Long = 0, var cron: ChronoUnit = ChronoUnit.MINUTES)
}
