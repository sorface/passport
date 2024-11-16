package by.sorface.passport.web.config.security.slo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Objects;

public class OidcPostRedirectLocationLogoutHandler implements LogoutHandler {
    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OidcLogoutAuthenticationToken oidcLogoutAuthenticationToken) {
            final String postLogoutRedirectUri = oidcLogoutAuthenticationToken.getPostLogoutRedirectUri();

            if (Objects.nonNull(postLogoutRedirectUri)) {
                response.sendRedirect(postLogoutRedirectUri);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
