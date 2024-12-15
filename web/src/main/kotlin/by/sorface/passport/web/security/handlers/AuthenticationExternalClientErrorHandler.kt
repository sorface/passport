package by.sorface.passport.web.security.handlers

import by.sorface.passport.web.config.options.EndpointProperties
import by.sorface.passport.web.services.locale.LocaleService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationExternalClientErrorHandler(private val endpointProperties: EndpointProperties, private val localeService: LocaleService) :
    AuthenticationFailureHandler {

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        if (exception is OAuth2AuthorizationCodeRequestAuthenticationException) {
            val error = exception.error

            if (Objects.isNull(error)) {
                response.sendRedirect(endpointProperties.uriPageFailure + "?error=" + exception.message)
                return
            }

            val localMessageByErrorCode = getLocalMessageByErrorCode(error)

            val message = localeService.getI18Message(localMessageByErrorCode)

            response.sendRedirect(endpointProperties.uriPageFailure + "?message=" + message + "&description=" + error.description)

            return
        }

        response.sendRedirect(endpointProperties.uriPageFailure + "?error=" + exception.message)
    }

    private fun getLocalMessageByErrorCode(error: OAuth2Error): String {
        val errorCode = listOf(
            OAuth2ErrorCodes.INVALID_REQUEST,
            OAuth2ErrorCodes.UNAUTHORIZED_CLIENT,
            OAuth2ErrorCodes.ACCESS_DENIED,
            OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE,
            OAuth2ErrorCodes.INVALID_SCOPE,
            OAuth2ErrorCodes.INSUFFICIENT_SCOPE,
            OAuth2ErrorCodes.INVALID_TOKEN,
            OAuth2ErrorCodes.SERVER_ERROR,
            OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE,
            OAuth2ErrorCodes.INVALID_CLIENT,
            OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE,
            OAuth2ErrorCodes.UNSUPPORTED_TOKEN_TYPE,
            OAuth2ErrorCodes.INVALID_REDIRECT_URI
        )
            .firstOrNull { it: String -> it.equals(error.errorCode, ignoreCase = true) }

        return if (errorCode != null) "client.error.$errorCode" else "client.error.${OAuth2ErrorCodes.SERVER_ERROR}"
    }
}
