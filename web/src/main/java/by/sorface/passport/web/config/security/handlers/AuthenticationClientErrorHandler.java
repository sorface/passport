package by.sorface.passport.web.config.security.handlers;

import by.sorface.passport.web.config.options.EndpointOptions;
import by.sorface.passport.web.services.locale.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationClientErrorHandler implements AuthenticationFailureHandler {

    /**
     * The {@link EndpointOptions} object that contains the URI of the failure page.
     */
    private final EndpointOptions endpointOptions;

    /**
     * The {@link LocaleService} object that provides the current locale.
     */
    private final LocaleService localeService;

    /**
     * This method is called when an authentication failure occurs. It checks if the exception is an instance of
     * {@link OAuth2AuthorizationCodeRequestAuthenticationException}. If it is, it retrieves the error from the exception,
     * gets the localized message for the error code, and redirects the user to the failure page with the message and description.
     * If the exception is not an instance of {@link OAuth2AuthorizationCodeRequestAuthenticationException}, it logs the error
     * and redirects the user to the failure page with the exception message.
     *
     * @param request   the {@link HttpServletRequest} object containing the client request
     * @param response  the {@link HttpServletResponse} object that contains the server response
     * @param exception the {@link AuthenticationException} object that contains the authentication failure
     * @throws IOException if an I/O exception occurred
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof OAuth2AuthorizationCodeRequestAuthenticationException) {
            final OAuth2Error error = ((OAuth2AuthorizationCodeRequestAuthenticationException) exception).getError();

            if (Objects.isNull(error)) {
                response.sendRedirect(endpointOptions.getUriPageFailure().concat("?error=").concat(exception.getMessage()));
                return;
            }

            final String localMessageByErrorCode = getLocalMessageByErrorCode(error);

            final var message = localeService.getI18Message(localMessageByErrorCode);

            response.sendRedirect(endpointOptions.getUriPageFailure().concat("?message=").concat(message).concat("&description=").concat(error.getDescription()));

            return;
        }

        log.error("Error client -> ", exception);
        response.sendRedirect(endpointOptions.getUriPageFailure().concat("?error=").concat(exception.getMessage()));
    }

    /**
     * This method returns the localized message for the given error code.
     *
     * @param error the {@link OAuth2Error} object that contains the error code
     * @return the localized message for the error code
     */
    private String getLocalMessageByErrorCode(final OAuth2Error error) {
        return Stream.of(
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
                .filter(it -> it.equalsIgnoreCase(error.getErrorCode()))
                .findFirst()
                .map("client.error."::concat)
                .orElse("client.error." + OAuth2ErrorCodes.SERVER_ERROR);
    }
}
