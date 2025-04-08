package by.sorface.idp.config.security.oauth2.slo

import by.sorface.idp.config.security.session.SessionManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    private val sessionManager: SessionManager
) : LogoutHandler {

    private val logger: Logger = LoggerFactory.getLogger(OidcWebSessionLogoutHandler::class.java)

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        if (authentication is OidcLogoutAuthenticationToken) {
            authentication.sessionId?.let { sessionId ->
                sessionManager.resetWithNotify(sessionId, authentication.name)
            }
        } else {
            logger.info("not use logout from user authorization and global session")
        }
    }

}
