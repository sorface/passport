package by.sorface.passport.web.config.security.csrf

import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class LocaleCookieFilter : OncePerRequestFilter() {

    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val locale = LocaleContextHolder.getLocale()

        response.addCookie(Cookie("lang", locale.toString()))

        filterChain.doFilter(request, response)
    }

}