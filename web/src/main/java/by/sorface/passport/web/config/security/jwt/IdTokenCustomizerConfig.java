package by.sorface.passport.web.config.security.jwt;

import by.sorface.passport.web.records.principals.DefaultPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
public class IdTokenCustomizerConfig {

    private final static String CLAIMS_USER_ID_NAME = "principal-id";

    private final static String CLAIMS_ROLES_NAME = "roles";

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue()) || OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> claims.putAll(buildCustomClaims(context)));
            }
        };
    }

    private Map<String, Object> buildCustomClaims(final JwtEncodingContext context) {
        final var principal = (DefaultPrincipal) context.getPrincipal().getPrincipal();

        final Set<String> roles = principal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Map.of(CLAIMS_ROLES_NAME, roles, CLAIMS_USER_ID_NAME, principal.getId());
    }
}
