package by.devpav.kotlin.oidcidp.config.security.csrf

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import java.util.function.Supplier

class SpaCsrfTokenRequestHandler(private val delegate: CsrfTokenRequestHandler) : CsrfTokenRequestAttributeHandler() {

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, csrfToken: Supplier<CsrfToken>) {
        delegate.handle(request, response, csrfToken)
    }

    override fun resolveCsrfTokenValue(request: HttpServletRequest, csrfToken: CsrfToken): String? {
        val csrfHeaderValue = request.getHeader(csrfToken.headerName)

        if (csrfHeaderValue.isNullOrBlank()) {
            return delegate.resolveCsrfTokenValue(request, csrfToken)
        }

        return super.resolveCsrfTokenValue(request, csrfToken)
    }

}