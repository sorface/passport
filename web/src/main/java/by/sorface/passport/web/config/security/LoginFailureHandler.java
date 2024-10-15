package by.sorface.passport.web.config.security;

import by.sorface.passport.web.config.options.EndpointOptions;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private static final BiFunction<String, String, String> BUILDER_URL = (error, url) ->
            url + "?error=" + UriUtils.encode(error, StandardCharsets.US_ASCII);

    private final EndpointOptions endpointOptions;

    /**
     * The onAuthenticationFailure function is called when the authentication process fails.
     * This function will redirect to the sign in page with an error message.
     *
     * @param request   Get the request object
     * @param response  Redirect the user to a different page
     * @param exception Determine if the exception is a badcredentialsexception, which means that either the username or password was invalid
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        final var errorMessageCode = (exception instanceof BadCredentialsException) ? "password.or.username.invalid" : "authentication.invalid";

        String redirectUri = BUILDER_URL.apply(errorMessageCode, endpointOptions.getUriPageSignIn());

        response.sendRedirect(redirectUri);
    }

}
