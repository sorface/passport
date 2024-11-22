package by.sorface.passport.web.config.security.handlers;

import by.sorface.passport.web.config.options.EndpointOptions;
import by.sorface.passport.web.security.handlers.AuthenticationExternalClientErrorHandler;
import by.sorface.passport.web.services.locale.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationExternalClientErrorHandlerTest {

    @Mock
    private EndpointOptions endpointOptions;

    @Mock
    private LocaleService localeService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationExternalClientErrorHandler authenticationExternalClientErrorHandler;

    @Test
    public void testOnAuthenticationFailureWithOAuth2AuthorizationCodeRequestAuthenticationException() throws IOException {
        final var exception = mock(OAuth2AuthorizationCodeRequestAuthenticationException.class);
        final var error = mock(OAuth2Error.class);

        when(endpointOptions.getUriPageFailure()).thenReturn("http://localhost:8080");
        when(localeService.getI18Message(Mockito.anyString())).thenReturn("test");

        when(exception.getError()).thenReturn(error);
        when(error.getErrorCode()).thenReturn("INVALID_REQUEST");
        when(error.getDescription()).thenReturn("test_description");

        authenticationExternalClientErrorHandler.onAuthenticationFailure(request, response, exception);

        verify(response, times(1)).sendRedirect(anyString());
    }

    @Test
    public void testOnAuthenticationFailureWithOtherAuthenticationException() throws IOException {
        final var exception = mock(AuthenticationException.class);

        when(exception.getMessage()).thenReturn("test_message");
        when(endpointOptions.getUriPageFailure()).thenReturn("http://localhost:8080");

        authenticationExternalClientErrorHandler.onAuthenticationFailure(request, response, exception);

        verify(response, times(1)).sendRedirect(anyString());
    }
}


