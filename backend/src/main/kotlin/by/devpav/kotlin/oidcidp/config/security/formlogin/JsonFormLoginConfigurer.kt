package by.devpav.kotlin.oidcidp.config.security.formlogin

import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

/**
 * Конфигуратор для обработки входа в систему с использованием JSON формата.
 *
 * @param H тип билдера HttpSecurityBuilder
 */
class JsonFormLoginConfigurer<H : HttpSecurityBuilder<H>>
    : AbstractAuthenticationFilterConfigurer<H, JsonFormLoginConfigurer<H>, UsernamePasswordAuthenticationFilter>(JsonUsernamePasswordAuthenticationFilter(), null) {

    /**
     * Создает RequestMatcher для обработки URL для входа в систему.
     *
     * @param loginProcessingUrl URL для обработки входа в систему
     * @return RequestMatcher для обработки URL для входа в систему
     */
    override fun createLoginProcessingUrlMatcher(loginProcessingUrl: String?): RequestMatcher {
        return AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name())
    }

    /**
     * Устанавливает страницу входа в систему.
     *
     * @param loginPage URL страницы входа в систему
     * @return конфигуратор для обработки входа в систему
     */
    public override fun loginPage(loginPage: String?): JsonFormLoginConfigurer<H>? {
        return super.loginPage(loginPage)
    }

}