package by.sorface.passport.web.utils;

import by.sorface.passport.web.records.principals.DefaultPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import javax.annotation.Nullable;
import java.security.Principal;
import java.util.Objects;

import static by.sorface.passport.web.utils.LogicUtils.not;


@UtilityClass
public class OAuth2AuthorizationUtils {

    private final static String PRINCIPAL_ATTRIBUTE_KEY = Principal.class.getName();

    public boolean hasPrincipal(final OAuth2Authorization authentication) {
        return authentication.getAttributes().containsKey(PRINCIPAL_ATTRIBUTE_KEY);
    }

    @Nullable
    public DefaultPrincipal getPrincipal(final OAuth2Authorization authentication) {
        final boolean containsPrincipalAuthentication = hasPrincipal(authentication);

        if (not(containsPrincipalAuthentication)) {
            return null;
        }

        final Authentication authenticationPrincipal = authentication.getAttribute(PRINCIPAL_ATTRIBUTE_KEY);

        if (Objects.isNull(authenticationPrincipal)) {
            return null;
        }

        final Object principalObject = authenticationPrincipal.getPrincipal();

        if (not(principalObject instanceof DefaultPrincipal)) {
            return null;
        }

        return (DefaultPrincipal) principalObject;
    }

    public static boolean hasToken(OAuth2Authorization authorization, String token, @org.springframework.lang.Nullable OAuth2TokenType tokenType) {
        if (tokenType == null) {
            return matchesState(authorization, token) ||
                    matchesAuthorizationCode(authorization, token) ||
                    matchesAccessToken(authorization, token) ||
                    matchesIdToken(authorization, token) ||
                    matchesRefreshToken(authorization, token) ||
                    matchesDeviceCode(authorization, token) ||
                    matchesUserCode(authorization, token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return matchesState(authorization, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return matchesAuthorizationCode(authorization, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return matchesAccessToken(authorization, token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            return matchesIdToken(authorization, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return matchesRefreshToken(authorization, token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            return matchesDeviceCode(authorization, token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            return matchesUserCode(authorization, token);
        }
        return false;
    }

    public static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    public static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        final OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    public static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    public static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }

    public static boolean matchesIdToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OidcIdToken> idToken = authorization.getToken(OidcIdToken.class);
        return idToken != null && idToken.getToken().getTokenValue().equals(token);
    }

    public static boolean matchesDeviceCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                authorization.getToken(OAuth2DeviceCode.class);
        return deviceCode != null && deviceCode.getToken().getTokenValue().equals(token);
    }

    public static boolean matchesUserCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2UserCode> userCode =
                authorization.getToken(OAuth2UserCode.class);
        return userCode != null && userCode.getToken().getTokenValue().equals(token);
    }

}
