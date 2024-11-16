package by.sorface.passport.web.config.security.slo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelegateLogoutHandler implements AuthenticationSuccessHandler {
    private final List<LogoutHandler> delegates = new ArrayList<>();

    public DelegateLogoutHandler(final LogoutHandler... delegates) {
        this.delegates.addAll(Arrays.asList(delegates));
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        this.delegates.forEach(delegate -> delegate.logout(request, response, authentication));
    }
}
