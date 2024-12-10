package by.sorface.passport.web.security.config.formlogin

import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

class JsonFormLoginConfigurer<H : HttpSecurityBuilder<H>>
    : AbstractAuthenticationFilterConfigurer<H, JsonFormLoginConfigurer<H>, UsernamePasswordAuthenticationFilter>(JsonUsernamePasswordAuthenticationFilter(), null) {

    override fun createLoginProcessingUrlMatcher(loginProcessingUrl: String?): RequestMatcher {
        return AntPathRequestMatcher(loginProcessingUrl, "POST")
    }

    public override fun loginPage(loginPage: String?): JsonFormLoginConfigurer<H>? {
        return super.loginPage(loginPage)
    }

}