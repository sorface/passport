package by.sorface.idp.config.security.oauth2.slo

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Component

@Component
class OidcWebSessionLogoutHandler(
    private val sessionRepository: FindByIndexNameSessionRepository<out Session>,
    private val authorizationService: OAuth2AuthorizationService
) : LogoutHandler {

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        if (authentication is OidcLogoutAuthenticationToken) {
            authentication.sessionId?.let { sessionId ->
                sessionRepository.deleteById(sessionId)
            }

            val tokenValue = authentication.idTokenHint ?: return

            val oAuth2Authorization = authorizationService.findByToken(tokenValue, OAuth2TokenType(OidcParameterNames.ID_TOKEN))
                ?: return

            authorizationService.remove(oAuth2Authorization)
        }
    }

}
