package by.sorface.idp.config.security.csrf

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.filter.OncePerRequestFilter

private const val CSRF_ATTRIBUTE_NAME = "_csrf"

/**
 * Класс, представляющий фильтр для работы с CSRF-токенами.
 */
open class CsrfCookieFilter : OncePerRequestFilter() {

    /**
     * Метод, вызываемый при обработке запроса.
     *
     * @param request Объект HttpServletRequest.
     * @param response Объект HttpServletResponse.
     * @param filterChain Объект FilterChain.
     */
    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val csrfToken = request.getAttribute(CSRF_ATTRIBUTE_NAME) as CsrfToken

        csrfToken.token

        filterChain.doFilter(request, response)
    }

}