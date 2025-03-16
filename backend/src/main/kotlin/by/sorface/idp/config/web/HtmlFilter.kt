package by.sorface.idp.config.web

import by.sorface.idp.extencions.isHtmlRequest
import by.sorface.idp.extencions.isNotAPI
import by.sorface.idp.extencions.isNotOAuth2API
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Фильтр, который перенаправляет HTML-запросы на главную страницу.
 */
@Component
@Profile("frontend")
class HtmlFilter : OncePerRequestFilter() {

    /**
     * Переопределяет метод doFilterInternal для перенаправления HTML-запросов на главную страницу.
     *
     * @param request HTTP-запрос.
     * @param response HTTP-ответ.
     * @param filterChain цепочка фильтров.
     */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (request.isHtmlRequest() && request.isNotAPI() && request.isNotOAuth2API()) {
            val mutateRequestToIndexPage = mutateRequestToIndexPage(request)
            filterChain.doFilter(mutateRequestToIndexPage, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }

    /**
     * Метод для изменения запроса на главную страницу.
     *
     * @param request HTTP-запрос.
     * @return измененный HTTP-запрос.
     */
    private fun mutateRequestToIndexPage(request: HttpServletRequest): HttpServletRequest {
        return object : HttpServletRequestWrapper(request) {
            override fun getRequestURI(): String {
                return "$contextPath/index.html"
            }
        }
    }

}