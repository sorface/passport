package by.sorface.passport.web.config.security.handlers;

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2Authorization;
import by.sorface.passport.web.services.redis.RedisOAuth2AuthorizationDetailsService;
import by.sorface.passport.web.services.redis.RedisOAuth2AuthorizationService;
import by.sorface.passport.web.services.sessions.DefaultAccountSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRevocationSuccessHandler implements AuthenticationSuccessHandler {

    private final DefaultAccountSessionService defaultAccountSessionService;

    private final RedisOAuth2AuthorizationService authorizationService;

    private final RedisOAuth2AuthorizationDetailsService authorizationDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final String token = request.getParameter(OAuth2ParameterNames.TOKEN);

        if (Objects.isNull(token)) {
            log.error("Revoke. Token is null");
            return;
        }

        log.info("revoked token revocation success");

        // находим текущую авторизацию пользователя
        final var currentPrincipalAuthorization = authorizationDetailsService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if (Objects.isNull(currentPrincipalAuthorization)) {
            log.warn("Current principal authorization is null for token {}", token);
            return;
        }

        log.warn("Drop current user authorizations {}", token);

        for (var authorization : currentPrincipalAuthorization) {
            // получаем список всех текущих авторизаций пользователя
            List<RedisOAuth2Authorization> principalAuthorizations = authorizationDetailsService.findByPrincipleId(authorization.getId());

            // проходим по всем авторизациям и пытаемся удалить
            for (var joinAuthorization : principalAuthorizations) {
                final OAuth2Authorization oAuth2Authorization = authorizationService.findById(joinAuthorization.getId());

                if (Objects.isNull(oAuth2Authorization)) {
                    continue;
                }

                authorizationService.remove(oAuth2Authorization);
            }

            log.warn("Drop current user sessions [username -> {}]", authorization.getPrincipalUsername());

            // находим все сессии пользователя и удаляем
            final List<Session> sessions = defaultAccountSessionService.findByUsername(authorization.getPrincipalUsername());
            final List<String> sessionIds = sessions.stream().map(Session::getId).toList();

            defaultAccountSessionService.batchDelete(sessionIds);
        }

        response.setStatus(HttpStatus.OK.value());
    }

}
