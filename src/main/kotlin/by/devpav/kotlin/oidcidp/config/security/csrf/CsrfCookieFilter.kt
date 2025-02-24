package by.devpav.kotlin.oidcidp.config.security.csrf

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.filter.OncePerRequestFilter

private const val CSRF_ATTRIBUTE_NAME = "_csrf"

open class CsrfCookieFilter : OncePerRequestFilter() {

    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val csrfToken = request.getAttribute(CSRF_ATTRIBUTE_NAME) as CsrfToken

        csrfToken.token

        filterChain.doFilter(request, response)
    }

}