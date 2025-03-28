package by.sorface.idp.config.security.oauth2.slo

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken
import org.springframework.security.web.authentication.logout.LogoutHandler

class OidcPostRedirectLocationLogoutHandler : LogoutHandler {

    private val logger = LoggerFactory.getLogger(OidcPostRedirectLocationLogoutHandler::class.java)

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        logger.info("post redirect user ${authentication.name} after logout")

        if (authentication is OidcLogoutAuthenticationToken) {
            val postLogoutRedirectUri = authentication.postLogoutRedirectUri

            logger.info("logout post redirect url [$postLogoutRedirectUri] for user ${authentication.name}")

            if (postLogoutRedirectUri.isNullOrBlank().not()) {
                logger.info("use post logout redirect url to [$postLogoutRedirectUri] for user ${authentication.name}")
                response.sendRedirect(postLogoutRedirectUri)
            } else {
                logger.info("use response OK for user ${authentication.name}, become post logout url is null or empty")
                response.status = HttpServletResponse.SC_OK
            }
        }
    }

}
