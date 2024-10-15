package by.sorface.passport.web.config.security;

import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.responses.OperationError;
import by.sorface.passport.web.services.locale.LocaleI18Service;
import by.sorface.passport.web.services.sleuth.SleuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SleuthService sleuthService;

    private final LocaleI18Service localeI18Service;

    /**
     * The commence function is called when the user attempts to access a resource that requires authentication.
     * The function will return an error message in JSON format, and set the HTTP status code to 401 (Unauthorized).
     *
     * @param request       Get the request url and the httpservletresponse response parameter is used to set the response status code
     * @param response      Set the response status code and content type
     * @param authException Get the exception that caused the authentication process to fail
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        final var operationError = buildError(HttpStatus.UNAUTHORIZED, I18Codes.I18GlobalCodes.ACCESS_DENIED);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try (final var responseStream = response.getOutputStream()) {
            final var mapper = new ObjectMapper();

            mapper.writeValue(responseStream, operationError);

            responseStream.flush();
        }
    }

    private OperationError buildError(final HttpStatus status, final String message) {
        return new OperationError(localeI18Service.getI18Message(message), status.getReasonPhrase(), status.value(), sleuthService.getSpanId(), sleuthService.getTraceId());
    }

}
