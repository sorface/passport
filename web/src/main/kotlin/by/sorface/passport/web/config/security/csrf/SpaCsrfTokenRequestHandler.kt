package by.sorface.passport.web.config.security.csrf

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import org.springframework.util.StringUtils
import java.util.function.Supplier

@Slf4j
@RequiredArgsConstructor
open class SpaCsrfTokenRequestHandler(private val delegate: CsrfTokenRequestHandler) : CsrfTokenRequestAttributeHandler() {

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, csrfToken: Supplier<CsrfToken>) {
        delegate.handle(request, response, csrfToken)
    }

    override fun resolveCsrfTokenValue(request: HttpServletRequest, csrfToken: CsrfToken): String {
        if (StringUtils.hasText(request.getHeader(csrfToken.headerName))) {
            return super.resolveCsrfTokenValue(request, csrfToken)
        }

        return delegate.resolveCsrfTokenValue(request, csrfToken)
    }

}
