package by.sorface.passport.web.config.security.csrf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocaleCookieFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private LocaleCookieFilter localeCookieFilter;

    @Test
    public void testDoFilterInternal() throws ServletException, IOException {
        localeCookieFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).addCookie(any(Cookie.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }
}

