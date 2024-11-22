package by.sorface.passport.web.security.oauth2.slo

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken
import org.springframework.security.web.authentication.logout.LogoutHandler

class OidcPostRedirectLocationLogoutHandler : LogoutHandler {

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        if (authentication is OidcLogoutAuthenticationToken) {
            val postLogoutRedirectUri = authentication.postLogoutRedirectUri

            if (postLogoutRedirectUri.isNullOrBlank().not()) {
                response.sendRedirect(postLogoutRedirectUri)
            } else {
                response.status = HttpServletResponse.SC_OK
            }
        }
    }

}
