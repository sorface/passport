package by.sorface.idp.web.rest.controller.advices

import by.sorface.idp.records.I18Codes
import by.sorface.idp.service.I18Service
import by.sorface.idp.web.rest.controller.AccountController
import by.sorface.idp.web.rest.controller.AccountSessionController
import by.sorface.idp.web.rest.exceptions.I18RestException
import by.sorface.idp.web.rest.model.errors.RestError
import by.sorface.idp.web.rest.model.errors.RestValidateError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice(
    basePackageClasses = [
        AccountController::class,
        AccountSessionController::class
    ]
)
class ExceptionAdvice(private val i18Service: I18Service) {

    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)
    }

    @ExceptionHandler(value = [I18RestException::class])
    fun handleI18Exception(ex: I18RestException): ResponseEntity<RestError> {
        val message = i18Service.getI18MessageOrDefault(ex.i18Code, ex.i18Args)

        val httpStatus = ex.httpStatus

        val error = RestError(message = message, code = httpStatus.value())

        logger.error(message, ex)

        return ResponseEntity.status(httpStatus).body(error)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidateException(ex: MethodArgumentNotValidException): RestValidateError {
        val errors = ex.bindingResult.allErrors
            .map { error: ObjectError ->
                RestValidateError.ValidateError().apply {
                    this.field = (error as FieldError).field

                    var message = error.getDefaultMessage()?.let { i18Service.getI18Message(it) }

                    if (Objects.isNull(message)) {
                        message = error.getDefaultMessage()
                    }

                    this.message = message
                }
            }
            .toList()

        val i18Message = i18Service.getI18MessageOrDefault(I18Codes.I18GlobalCodes.VALIDATION_ERROR)

        logger.error(ex.message, ex)

        return RestValidateError(i18Message, errors)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(ex: Exception): ResponseEntity<RestError> {
        val message = i18Service.getI18MessageOrDefault(I18Codes.I18GlobalCodes.UNKNOWN_ERROR)

        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val error = RestError(message = message, code = httpStatus.value())

        logger.error(message, ex)

        return ResponseEntity.status(httpStatus).body(error)
    }

}
