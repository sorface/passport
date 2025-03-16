package by.sorface.idp.config.security.entrypoints

import by.sorface.idp.extencions.useErrorJsonStream
import by.sorface.idp.records.I18Codes
import by.sorface.idp.records.I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED
import by.sorface.idp.service.I18Service
import by.sorface.idp.web.rest.model.errors.RestError
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * Класс, представляющий точку входа для обработки неавторизованных запросов.
 *
 * @property i18Service Сервис для работы с интернационализацией.
 */
@Component
class JsonUnauthorizedAuthenticationEntryPoint(private val i18Service: I18Service) : AuthenticationEntryPoint {

    /**
     * Метод, вызываемый при неавторизованном запросе.
     *
     * @param request Объект HttpServletRequest.
     * @param response Объект HttpServletResponse.
     * @param authException Исключение, связанное с аутентификацией.
     */
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        val operationError = buildError(HttpStatus.UNAUTHORIZED, NOT_AUTHENTICATED)

        response.useErrorJsonStream(operationError)
    }

    /**
     * Метод для создания объекта ошибки.
     *
     * @param status Статус ошибки.
     * @param message Сообщение об ошибке.
     * @return Объект RestError.
     */
    private fun buildError(status: HttpStatus, message: String): RestError {
        val i18Message = i18Service.getI18MessageOrDefault(message, defaultCode = I18Codes.I18GlobalCodes.ACCESS_DENIED)

        return RestError(i18Message, status.value())
    }
}
