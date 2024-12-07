package by.sorface.passport.web.security.config.formlogin

import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.responses.OperationError
import by.sorface.passport.web.security.extensions.useErrorJsonStream
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.sleuth.SleuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class JsonFormLoginFailureHandler(private val sleuthService: SleuthService, private val localeI18Service: LocaleI18Service) : AuthenticationFailureHandler {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(JsonFormLoginFailureHandler::class.java)
    }

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val i18ErrorCode = when (exception) {
            is BadCredentialsException -> I18Codes.I18AuthenticationCodes.PASSWORD_OR_USERNAME_INVALID
            else -> I18Codes.I18AuthenticationCodes.UNKNOWN_ERROR
        }

        val operationError = buildError(HttpStatus.UNAUTHORIZED, i18ErrorCode)

        logger.error("exception [${exception.javaClass.simpleName}] during processing. message: ${exception.message}", exception)

        response.useErrorJsonStream(operationError)
    }

    private fun buildError(status: HttpStatus, message: String): OperationError {
        return OperationError(localeI18Service.getI18Message(message) ?: "", status.reasonPhrase, status.value(), sleuthService.getSpanId(), sleuthService.getTraceId())
    }

}
