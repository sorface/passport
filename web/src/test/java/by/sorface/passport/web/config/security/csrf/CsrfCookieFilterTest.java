package by.sorface.passport.web.config.security.csrf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsrfCookieFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private CsrfCookieFilter csrfCookieFilter;

    @Test
    public void testDoFilterInternal() throws ServletException, IOException {
        final var csrfToken = mock(CsrfToken.class);
        when(request.getAttribute("_csrf")).thenReturn(csrfToken);
        when(csrfToken.getToken()).thenReturn("tokenValue");

        csrfCookieFilter.doFilterInternal(request, response, filterChain);

        verify(csrfToken, times(1)).getToken();
        verify(filterChain, times(1)).doFilter(request, response);
    }
}