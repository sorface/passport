package by.sorface.passport.web.config.security.csrf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class represents a filter for handling Cross-Site Request Forgery (CSRF) protection.
 * It extends the {@link OncePerRequestFilter} class from Spring Security.
 */
public class CsrfCookieFilter extends OncePerRequestFilter {

    /**
     * This method is called for each request to the application after all filters have been applied but before the controller
     * is invoked. It retrieves the CSRF token from the request and ensures it is loaded.
     * Then it passes the request and response to the next filter in the chain.
     *
     * @param request the {@link HttpServletRequest} object containing the client request
     * @param response the {@link HttpServletResponse} object that contains the server response
     * @param filterChain the {@link FilterChain} object that contains the next filter in the chain
     * @throws ServletException if an input or output exception occurred
     * @throws IOException if an I/O exception occurred
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var csrfToken = (CsrfToken) request.getAttribute("_csrf");

        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.getToken();

        filterChain.doFilter(request, response);
    }

}