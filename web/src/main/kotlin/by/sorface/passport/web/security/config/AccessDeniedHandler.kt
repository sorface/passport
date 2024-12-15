package by.sorface.passport.web.security.config

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
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.InvalidCsrfTokenException
import org.springframework.stereotype.Component

@Component
class AccessDeniedHandler(private val sleuthService: SleuthService, private val localeI18Service: LocaleI18Service) :
    AccessDeniedHandler {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccessDeniedHandler::class.java)
    }

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        val i18ErrorCode = when (accessDeniedException) {
            is InvalidCsrfTokenException -> I18Codes.I18CsrfCodes.INVALID
            else -> I18Codes.I18GlobalCodes.ACCESS_DENIED
        }

        val operationError = buildError(HttpStatus.FORBIDDEN, i18ErrorCode)

        logger.info("exception [${accessDeniedException.javaClass.simpleName}] during processing. message -> ${accessDeniedException.message}")

        response.useErrorJsonStream(operationError)
    }

    private fun buildError(status: HttpStatus, message: String): OperationError {
        return OperationError(localeI18Service.getI18Message(message) ?: "", status.reasonPhrase, status.value(), sleuthService.getSpanId(), sleuthService.getTraceId())
    }
}