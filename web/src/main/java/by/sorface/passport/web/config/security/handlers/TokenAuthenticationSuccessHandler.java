package by.sorface.passport.web.config.security.handlers;

import by.sorface.passport.web.exceptions.ObjectInvalidException;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.records.tokens.IntrospectionPrincipal;
import by.sorface.passport.web.records.tokens.TokenRecord;
import by.sorface.passport.web.utils.OAuth2AuthorizationUtils;
import by.sorface.passport.web.utils.json.Json;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    /**
     * The onAuthenticationSuccess function is called when the authentication process succeeds.
     * It takes in a request, response, and authentication object as parameters.
     * The function then creates a token using the getToken function (which we will define later).
     * Then it creates an HttpMessageConverter that can convert Java objects to JSON strings.
     *
     * @param request        Get the httpservletrequest object
     * @param response       Write the token to the response body
     * @param authentication Get the token
     *                       private tokenresponse gettoken(final oauth2tokenintrospectionauthenticationtoken authentication) {
     *                       final var principal = (oauth2user) authentication
     */
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        final var token = getToken((OAuth2TokenIntrospectionAuthenticationToken) authentication);
        final var message = new ServletServerHttpResponse(response);

        mappingJackson2HttpMessageConverter.write(token, MediaType.APPLICATION_JSON, message);
    }

    /**
     * The getToken function is responsible for retrieving the token record from the database.
     * <p>
     * The function takes in an OAuth2TokenIntrospectionAuthenticationToken object and returns a TokenRecord object.
     * The function first creates a new TokenRecordBuilder object, which will be used to build our final TokenRecord object.
     * <p>
     * Next, we check if the token is active by checking if it's active property is true or false.
     * If it's not active, then we return an empty TokenRecordBuilder with its &quot;active&quot; property set to false (line 12).
     * Otherwise, we continue on with building our final token record (lines 14-15)
     *
     * @param authenticationToken Get the token claims
     * @return An object of type tokenrecord
     */
    private TokenRecord getToken(final OAuth2TokenIntrospectionAuthenticationToken authenticationToken) {
        final String token = authenticationToken.getToken();

        log.info("introspect. receive token with value {}", Json.lazyStringifyWithMasking(Map.of("token", token)));

        final var tokenRecordBuilder = TokenRecord.builder().active(false);

        if (!authenticationToken.getTokenClaims().isActive()) {
            log.info("token is not active for with token {}", Json.lazyStringifyWithMasking(Map.of("token", token)));

            return tokenRecordBuilder.build();
        }

        final OAuth2TokenIntrospection claims = authenticationToken.getTokenClaims();

        tokenRecordBuilder
                .active(true)
                .sub(claims.getSubject())
                .aud(claims.getAudience())
                .nbf(claims.getNotBefore())
                .scopes(claims.getScopes())
                .iss(claims.getIssuer())
                .exp(claims.getExpiresAt())
                .iat(claims.getIssuedAt())
                .jti(claims.getId())
                .clientId(claims.getClientId())
                .tokenType(claims.getTokenType());

        log.info("introspect. get oauth2 authorization by token {}", Json.lazyStringifyWithMasking(Map.of("token", token)));

        final var oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if (Objects.isNull(oAuth2Authorization)) {
            log.info("introspect. token [value -> {}] is not active, because oath2 authorization is null", Json.lazyStringifyWithMasking((Map.of("token", token))));

            return tokenRecordBuilder.build();
        }

        log.info("introspect. receive oauth2 authorization has been completed successfully for user {}", oAuth2Authorization.getPrincipalName());

        final DefaultPrincipal principal = OAuth2AuthorizationUtils.getPrincipal(oAuth2Authorization);

        if (Objects.isNull(principal)) {
            throw new ObjectInvalidException(I18Codes.I18GlobalCodes.ACCESS_DENIED, Map.of("objectClass", DefaultPrincipal.class.getName()));
        }

        log.info("introspect of the token for the user has been completed successfully.  {}",
                Json.lazyStringifyWithMasking(Map.of("user_id", principal.getId(), "username", principal.getUsername(), "token", token)));

        final var introspectionPrincipal = IntrospectionPrincipal.from(principal);

        return tokenRecordBuilder.principal(introspectionPrincipal).build();
    }

}
