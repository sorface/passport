package by.sorface.passport.web.controllers.advices;

import by.sorface.passport.web.controllers.AccountController;
import by.sorface.passport.web.controllers.AccountSessionController;
import by.sorface.passport.web.controllers.ApplicationClientController;
import by.sorface.passport.web.controllers.CsrfController;
import by.sorface.passport.web.exceptions.*;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.responses.OperationError;
import by.sorface.passport.web.records.responses.ValidateOperationError;
import by.sorface.passport.web.services.locale.LocaleI18Service;
import by.sorface.passport.web.services.sleuth.SleuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice(
        basePackageClasses = {
                AccountController.class,
                AccountSessionController.class,
                ApplicationClientController.class,
                CsrfController.class
        }
)
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final SleuthService sleuthService;

    private final LocaleI18Service localeI18Service;

    @ExceptionHandler(value = {
            GlobalSystemException.class,
            UserRequestException.class,
            NotFoundException.class,
            ObjectExpiredException.class,
            ObjectInvalidException.class,
            UnauthorizedException.class
    })
    public ResponseEntity<OperationError> handleUserRequestException(final GlobalSystemException e) {
        final OperationError body = buildError(e, sleuthService.getSpanId(), sleuthService.getTraceId());

        return ResponseEntity.status(e.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidateOperationError handleException(final MethodArgumentNotValidException e) {
        final List<ValidateOperationError.ValidateError> errors = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    final var validateError = new ValidateOperationError.ValidateError();
                    {
                        validateError.setField(((FieldError) error).getField());

                        String message = localeI18Service.getI18Message(error.getDefaultMessage());

                        if (Objects.isNull(message)) {
                            message = error.getDefaultMessage();
                        }

                        validateError.setMessage(message);
                    }

                    return validateError;
                })
                .toList();

        return buildValidateError(HttpStatus.BAD_REQUEST, errors, sleuthService.getSpanId(), sleuthService.getTraceId());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OperationError handleAccessDenied(final AccessDeniedException e) {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage(), sleuthService.getSpanId(), sleuthService.getTraceId());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OperationError handleInsufficientAuthentication(final InsufficientAuthenticationException e) {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage(), sleuthService.getSpanId(), sleuthService.getTraceId());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public OperationError handleAuthenticationException(final AuthenticationException e) {
        return buildError(HttpStatus.FORBIDDEN, e.getMessage(), sleuthService.getSpanId(), sleuthService.getTraceId());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperationError handleNotFoundException(final RuntimeException e) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, I18Codes.I18GlobalCodes.UNKNOWN_ERROR, sleuthService.getSpanId(), sleuthService.getTraceId());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperationError handleException(final Exception e) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, I18Codes.I18GlobalCodes.UNKNOWN_ERROR, sleuthService.getSpanId(), sleuthService.getTraceId());
    }

    private OperationError buildError(final GlobalSystemException exception, final String spanId, final String traceId) {
        final String message = localeI18Service.getI18Message(exception.getI18Code(), exception.getArgs());
        return new OperationError(message, exception.getHttpStatus().getReasonPhrase(), exception.getHttpStatus().value(), spanId, traceId);
    }

    private OperationError buildError(final HttpStatus status, final String message, final String spanId, final String traceId) {
        return new OperationError(localeI18Service.getI18Message(message), status.getReasonPhrase(), status.value(), spanId, traceId);
    }

    private ValidateOperationError buildValidateError(final HttpStatus status,
                                                      final List<ValidateOperationError.ValidateError> errors,
                                                      final String spanId,
                                                      final String traceId) {
        return new ValidateOperationError(localeI18Service.getI18Message(I18Codes.I18GlobalCodes.VALIDATION_ERROR), errors, status.value(), spanId, traceId);
    }
}
