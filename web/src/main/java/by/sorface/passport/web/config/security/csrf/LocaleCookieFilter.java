package by.sorface.passport.web.config.security.csrf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class represents a filter for setting the locale in a cookie.
 * It extends the {@link OncePerRequestFilter} class from Spring Security.
 */
public class LocaleCookieFilter extends OncePerRequestFilter {

    /**
     * This method is called for each request to the application after all filters have been applied but before the controller
     * is invoked. It retrieves the current locale from the {@link LocaleContextHolder} and adds a cookie to the response
     * with the locale as its value. Then it passes the request and response to the next filter in the chain.
     *
     * @param request the {@link HttpServletRequest} object containing the client request
     * @param response the {@link HttpServletResponse} object that contains the server response
     * @param filterChain the {@link FilterChain} object that contains the next filter in the chain
     * @throws ServletException if an input or output exception occurred
     * @throws IOException if an I/O exception occurred
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var locale = LocaleContextHolder.getLocale();

        response.addCookie(new Cookie("lang", locale.toString()));

        filterChain.doFilter(request, response);
    }

}