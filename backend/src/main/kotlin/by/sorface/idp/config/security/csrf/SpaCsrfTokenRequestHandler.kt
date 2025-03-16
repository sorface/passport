package by.sorface.idp.config.security.csrf

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import java.util.function.Supplier

/**
 * Класс SpaCsrfTokenRequestHandler, который расширяет CsrfTokenRequestAttributeHandler.
 * Он используется для обработки CSRF-токенов в приложении.
 *
 * @param delegate экземпляр CsrfTokenRequestHandler, который будет использоваться для обработки CSRF-токенов.
 */
class SpaCsrfTokenRequestHandler(private val delegate: CsrfTokenRequestHandler) : CsrfTokenRequestAttributeHandler() {

    /**
     * Метод handle, который вызывает метод handle делегата для обработки CSRF-токена.
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе.
     * @param response объект HttpServletResponse, содержащий информацию о ответе.
     * @param csrfToken поставщик CSRF-токена.
     */
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, csrfToken: Supplier<CsrfToken>) {
        delegate.handle(request, response, csrfToken)
    }

    /**
     * Метод resolveCsrfTokenValue, который разрешает значение CSRF-токена.
     * Если заголовок CSRF-токена пустой или null, то вызывается метод resolveCsrfTokenValue делегата.
     * В противном случае вызывается метод resolveCsrfTokenValue суперкласса.
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе.
     * @param csrfToken объект CSRF-токена.
     * @return значение CSRF-токена.
     */
    override fun resolveCsrfTokenValue(request: HttpServletRequest, csrfToken: CsrfToken): String? {
        val csrfHeaderValue = request.getHeader(csrfToken.headerName)

        if (csrfHeaderValue.isNullOrBlank()) {
            return delegate.resolveCsrfTokenValue(request, csrfToken)
        }

        return super.resolveCsrfTokenValue(request, csrfToken)
    }

}