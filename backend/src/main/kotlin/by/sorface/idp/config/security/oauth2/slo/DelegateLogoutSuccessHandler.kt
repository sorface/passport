package by.sorface.passport.web.security.oauth2.slo

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutHandler

open class DelegateLogoutSuccessHandler(vararg delegates: LogoutHandler) : AuthenticationSuccessHandler {

    private val delegates: MutableList<LogoutHandler> = ArrayList()

    init {
        this.delegates.addAll(listOf(*delegates))
    }

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        delegates.forEach { delegate: LogoutHandler -> delegate.logout(request, response, authentication) }
    }

}
