package by.sorface.passport.web.api.advices

import by.sorface.passport.web.api.AccountController
import by.sorface.passport.web.api.AccountSessionController
import by.sorface.passport.web.api.ApplicationClientController
import by.sorface.passport.web.exceptions.*
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.responses.OperationError
import by.sorface.passport.web.records.responses.ValidateOperationError
import by.sorface.passport.web.records.responses.ValidateOperationError.ValidateError
import by.sorface.passport.web.security.csrf.CsrfController
import by.sorface.passport.web.services.locale.LocaleI18Service
import by.sorface.passport.web.services.sleuth.SleuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice(
    basePackageClasses = [AccountController::class, AccountSessionController::class, ApplicationClientController::class, CsrfController::class
    ]
)
class ExceptionAdvice(private val sleuthService: SleuthService, private val localeI18Service: LocaleI18Service) {

    @ExceptionHandler(
        value = [
            GlobalSystemException::class,
            UserRequestException::class,
            NotFoundException::class,
            ObjectExpiredException::class,
            ObjectInvalidException::class,
            UnauthorizedException::class
        ]
    )
    fun handleUserRequestException(e: GlobalSystemException): ResponseEntity<OperationError> {
        val body = buildError(e, sleuthService.getSpanId(), sleuthService.getTraceId())

        return ResponseEntity.status(e.httpStatus).body(body)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: MethodArgumentNotValidException): ValidateOperationError {
        val errors = e.bindingResult.allErrors.stream()
            .map { error: ObjectError ->
                val validateError = ValidateError()
                run {
                    validateError.field = (error as FieldError).field
                    var message = error.getDefaultMessage()?.let { localeI18Service.getI18Message(it) }

                    if (Objects.isNull(message)) {
                        message = error.getDefaultMessage()
                    }
                    validateError.message = message
                }
                validateError
            }
            .toList()

        return buildValidateError(HttpStatus.BAD_REQUEST, errors, sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAccessDenied(e: AccessDeniedException): OperationError {
        return buildError(HttpStatus.UNAUTHORIZED, e.message, sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    @ExceptionHandler(InsufficientAuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInsufficientAuthentication(e: InsufficientAuthenticationException): OperationError {
        return buildError(HttpStatus.UNAUTHORIZED, e.message, sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAuthenticationException(e: AuthenticationException): OperationError {
        return buildError(HttpStatus.FORBIDDEN, e.message, sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleNotFoundException(e: RuntimeException?): OperationError {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, I18Codes.I18GlobalCodes.UNKNOWN_ERROR, sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception?): OperationError {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, I18Codes.I18GlobalCodes.UNKNOWN_ERROR, sleuthService.getSpanId(), sleuthService.getTraceId())
    }

    private fun buildError(exception: GlobalSystemException, spanId: String?, traceId: String?): OperationError {
        val message = localeI18Service.getI18Message(exception.i18Code, exception.args)

        return OperationError(message!!, exception.httpStatus.reasonPhrase, exception.httpStatus.value(), spanId!!, traceId!!)
    }

    private fun buildError(status: HttpStatus, message: String?, spanId: String?, traceId: String?): OperationError {
        val errorMessage = message?.let { localeI18Service.getI18Message(it) } ?: localeI18Service.getI18Message(I18Codes.I18GlobalCodes.UNKNOWN_ERROR)

        return OperationError(errorMessage!!, status.reasonPhrase, status.value(), spanId, traceId)
    }

    private fun buildValidateError(status: HttpStatus, errors: List<ValidateError>, spanId: String?, traceId: String?): ValidateOperationError {
        return ValidateOperationError(localeI18Service.getI18Message(I18Codes.I18GlobalCodes.VALIDATION_ERROR)!!, errors, status.value(), spanId, traceId)
    }
}