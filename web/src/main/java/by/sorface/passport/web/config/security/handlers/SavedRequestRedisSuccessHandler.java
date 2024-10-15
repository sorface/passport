package by.sorface.passport.web.config.security.handlers;

import by.sorface.passport.web.config.options.EndpointOptions;
import by.sorface.passport.web.constants.SessionAttributes;
import by.sorface.passport.web.utils.json.Json;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SavedRequestRedisSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements AuthenticationSuccessHandler {

    private final EndpointOptions endpointOptions;

    @PostConstruct
    public void init() {
        setTargetUrlParameter("targetUrl");
        setDefaultTargetUrl(endpointOptions.getUriPageProfile());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("request session id -> {}", request.getRequestedSessionId());
        log.info("authorized user -> {}{}", System.lineSeparator(), Json.lazyStringify(authentication.getPrincipal()));

        final var requestAttributes = RequestContextHolder.currentRequestAttributes();

        final var savedRequest = (SavedRequest) requestAttributes.getAttribute(SessionAttributes.SAVED_REQUEST, RequestAttributes.SCOPE_SESSION);

        final Optional<String> userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT));

        if (userAgent.isPresent()) {
            String userAgentLine = userAgent.get();

            log.debug("user-agent [value -> {}] for session [id -> {}]", request.getRequestedSessionId(), userAgentLine);

            requestAttributes.setAttribute(SessionAttributes.USER_AGENT, userAgentLine, RequestAttributes.SCOPE_SESSION);
        }

        if (Objects.isNull(savedRequest)) {
            log.info("saved request is NULL for session [id -> {}]", request.getRequestedSessionId());

            super.handle(request, response, authentication);
            return;
        }

        log.info("found saved request [url -> {}]. session [id -> {}]", savedRequest.getRedirectUrl(), request.getRequestedSessionId());
        String targetUrlParameter = getTargetUrlParameter();
        log.info("target url parameter [{}], session [id -> {}]", targetUrlParameter, request.getRequestedSessionId());

        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            super.handle(request, response, authentication);
            return;
        }

        log.info("clean session authentication for session [id -> {}]", request.getRequestedSessionId());
        clearAuthenticationAttributes(requestAttributes);
        String targetUrl = savedRequest.getRedirectUrl();
        log.info("oauth2 redirect to target url -> {}. session [id -> {}]", targetUrl, request.getRequestedSessionId());
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public void clearAuthenticationAttributes(RequestAttributes requestAttributes) {
        Object attribute = requestAttributes.getAttribute(SessionAttributes.AUTHENTICATION_EXCEPTION, RequestAttributes.SCOPE_SESSION);

        if (Objects.nonNull(attribute)) {
            requestAttributes.removeAttribute(SessionAttributes.AUTHENTICATION_EXCEPTION, RequestAttributes.SCOPE_SESSION);
        }
    }

    @Override
    protected boolean isAlwaysUseDefaultTargetUrl() {
        return false;
    }
}
