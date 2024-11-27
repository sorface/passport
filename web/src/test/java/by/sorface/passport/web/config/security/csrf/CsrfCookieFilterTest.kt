package by.sorface.passport.web.config.security.csrf

import by.sorface.passport.web.security.config.CsrfConfiguration
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.web.csrf.CsrfToken
import java.io.IOException

@ExtendWith(MockitoExtension::class)
class CsrfCookieFilterTest {

    @Mock
    lateinit var request: HttpServletRequest

    @Mock
    lateinit var response: HttpServletResponse

    @Mock
    lateinit var filterChain: FilterChain

    @InjectMocks
    lateinit var csrfCookieFilter: CsrfConfiguration.CsrfCookieFilter

    @Test
    @Throws(ServletException::class, IOException::class)
    fun testDoFilterInternal() {
        val csrfToken: CsrfToken = Mockito.mock(CsrfToken::class.java)
        Mockito.`when`(request.getAttribute("_csrf")).thenReturn(csrfToken)
        Mockito.`when`(csrfToken.token).thenReturn("tokenValue")

        csrfCookieFilter.doFilterInternal(request, response, filterChain)

        Mockito.verify(csrfToken, Mockito.times(1)).token
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response)
    }
}