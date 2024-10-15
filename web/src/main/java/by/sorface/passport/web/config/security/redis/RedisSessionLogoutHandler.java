package by.sorface.passport.web.config.security.redis;

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2Authorization;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.services.redis.RedisOAuth2AuthorizationDetailsService;
import by.sorface.passport.web.services.redis.RedisOAuth2AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static by.sorface.passport.web.utils.LogicUtils.not;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSessionLogoutHandler implements LogoutHandler {

    private final RedisOAuth2AuthorizationService authorizationService;

    private final RedisOAuth2AuthorizationDetailsService authorizationDetailsService;

    /**
     * The logout function is responsible for deleting the session from Redis.
     * It does this by first deleting the expires key, which is a key that contains
     * an expiration time for the session. This prevents any future access to this
     * session in Redis, even if it's still valid (i.e., not expired). Next, we delete
     * the index key and then finally delete the actual session data itself from Redis.
     *
     * @param request        Get the session id from the request
     * @param response       Set the cookie to expire
     * @param authentication Get the user's id
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (Objects.isNull(authentication)) {
            return;
        }

        if (Objects.isNull(authentication.getPrincipal())) {
            return;
        }

        if (not(authentication.getPrincipal() instanceof DefaultPrincipal)) {
            return;
        }

        final var authorizedId = ((DefaultPrincipal) authentication.getPrincipal()).getId();

        if (Objects.isNull(authorizedId)) {
            return;
        }

        final List<RedisOAuth2Authorization> principalAuthorizations = authorizationDetailsService.findByPrincipleId(authorizedId.toString());

        for (var principalAuthorization : principalAuthorizations) {
            final var authorization = authorizationService.findById(principalAuthorization.getId());

            if (Objects.isNull(authorization)) {
                continue;
            }

            authorizationService.remove(authorization);
        }
    }

}
