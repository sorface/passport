package by.devpav.kotlin.oidcidp.config.security.formlogin

import by.devpav.kotlin.oidcidp.extencions.useErrorJsonStream
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.service.I18Service
import by.devpav.kotlin.oidcidp.web.rest.model.errors.RestError
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

/**
 * Обработчик ошибок входа в систему
 *
 * @param i18Service сервис для работы с интернационализацией
 */
@Component
class JsonFormLoginFailureHandler(private val i18Service: I18Service) : AuthenticationFailureHandler {

    private val logger: Logger = LoggerFactory.getLogger(JsonFormLoginFailureHandler::class.java)

    /**
     * Обрабатывает ошибку входа в систему.
     *
     * @param request запрос
     * @param response ответ
     * @param exception исключение, которое произошло при входе в систему
     */
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val i18ErrorCode = when (exception) {
            is BadCredentialsException -> I18Codes.I18AuthenticationCodes.PASSWORD_OR_USERNAME_INVALID
            else -> I18Codes.I18AuthenticationCodes.UNKNOWN_ERROR
        }

        val operationError = buildError(HttpStatus.UNAUTHORIZED, i18ErrorCode)

        logger.error("exception [${exception.javaClass.simpleName}] during processing. message: ${exception.message}", exception)

        response.useErrorJsonStream(operationError)
    }

    /**
     * Создает объект ошибки.
     *
     * @param status статус ошибки
     * @param message сообщение об ошибке
     * @return объект ошибки
     */
    private fun buildError(status: HttpStatus, message: String): RestError {
        val i18Message = i18Service.getI18MessageOrDefault(message, defaultCode = I18Codes.I18GlobalCodes.ACCESS_DENIED)

        return RestError(i18Message,status.value())
    }
}
