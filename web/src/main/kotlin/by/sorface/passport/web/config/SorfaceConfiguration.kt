package by.sorface.passport.web.config

import brave.sampler.Sampler
import by.sorface.passport.web.constants.SupportedLocales
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.nio.charset.StandardCharsets

@Configuration
class SorfaceConfiguration {

    @Bean
    fun resourceBundleMessageSource(): ResourceBundleMessageSource = ResourceBundleMessageSource().apply {
        setBasename(I18_BUNDLE_LOCATION)
        setDefaultEncoding(StandardCharsets.UTF_8.name())
    }

    @Bean
    fun userAgentAnalyzer(): UserAgentAnalyzer = UserAgentAnalyzer.newBuilder()
        .hideMatcherLoadStats()
        .withoutCache()
        .build()

    @Bean
    fun defaultSampler(): Sampler = Sampler.ALWAYS_SAMPLE

    @Bean
    fun localeResolver(): LocaleResolver = AcceptHeaderLocaleResolver().apply {
        supportedLocales = SupportedLocales.entries.map { it.locale }
        setDefaultLocale(SupportedLocales.EN.locale)
    }

    @Bean
    fun accountCookieBuilder(accountCookieProperties: AccountCookieProperties): (otpId: String, maxAge: Int) -> Cookie = { otpId, maxAge ->
        Cookie(accountCookieProperties.name, otpId).apply {
            domain = accountCookieProperties.domain
            path = accountCookieProperties.path
            secure = accountCookieProperties.secure
            isHttpOnly = accountCookieProperties.httpOnly
            setMaxAge(maxAge)
        }
    }

    @Bean
    fun accountCookieValue(accountCookieProperties: AccountCookieProperties): (request: HttpServletRequest) -> String? = funcCookie@{ request: HttpServletRequest ->
        return@funcCookie request.cookies.firstOrNull { it.name == accountCookieProperties.name }?.value ?: return@funcCookie null
    }


    companion object {
        const val I18_BUNDLE_LOCATION: String = "language/messages"
    }

}
