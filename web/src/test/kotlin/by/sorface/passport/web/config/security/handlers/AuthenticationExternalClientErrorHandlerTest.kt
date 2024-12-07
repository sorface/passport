package by.sorface.passport.web.config.security.handlers

import by.sorface.passport.web.config.options.EndpointProperties
import by.sorface.passport.web.security.handlers.AuthenticationExternalClientErrorHandler
import by.sorface.passport.web.services.locale.LocaleService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.AuthenticationException
import java.io.IOException

@ExtendWith(MockitoExtension::class)
class AuthenticationExternalClientErrorHandlerTest {
    @Mock
    private val endpointProperties: EndpointProperties? = null

    @Mock
    private val localeService: LocaleService? = null

    @Mock
    private val request: HttpServletRequest? = null

    @Mock
    private val response: HttpServletResponse? = null

    @InjectMocks
    private val authenticationExternalClientErrorHandler: AuthenticationExternalClientErrorHandler? = null

    @Test
    @Throws(IOException::class)
    fun testOnAuthenticationFailureWithOtherAuthenticationException() {
        val exception = Mockito.mock(AuthenticationException::class.java)

        Mockito.`when`(exception.message).thenReturn("test_message")
        Mockito.`when`(endpointProperties!!.uriPageFailure).thenReturn("http://localhost:8080")

        authenticationExternalClientErrorHandler!!.onAuthenticationFailure(request!!, response!!, exception)

        Mockito.verify(response, Mockito.times(1)).sendRedirect(ArgumentMatchers.anyString())
    }
}


