package by.sorface.passport.web.config.security

import by.sorface.passport.web.config.options.EndpointOptions
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.function.BiFunction

@Component
@RequiredArgsConstructor
class LoginFailureHandler(
    private val endpointOptions: EndpointOptions
) : AuthenticationFailureHandler {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val errorMessageCode = if ((exception is BadCredentialsException)) "password.or.username.invalid" else "authentication.invalid"

        val redirectUri = BUILDER_URL.apply(errorMessageCode, endpointOptions.uriPageSignIn)

        response.sendRedirect(redirectUri)
    }

    companion object {
        private val BUILDER_URL = BiFunction { error: String?, url: String? -> url + "?error=" + UriUtils.encode(error, StandardCharsets.US_ASCII) }
    }
}
