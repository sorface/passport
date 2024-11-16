package by.sorface.passport.web.utils

import by.sorface.passport.web.records.principals.DefaultPrincipal
import lombok.experimental.UtilityClass
import org.springframework.lang.Nullable
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2DeviceCode
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.OAuth2UserCode
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import java.security.Principal
import java.util.*

@UtilityClass
class OAuth2AuthorizationUtils {
    fun hasPrincipal(authentication: OAuth2Authorization): Boolean {
        return authentication.attributes.containsKey(PRINCIPAL_ATTRIBUTE_KEY)
    }

    fun getPrincipal(authentication: OAuth2Authorization): DefaultPrincipal? {
        val containsPrincipalAuthentication = hasPrincipal(authentication)

        if (LogicUtils.not(containsPrincipalAuthentication)) {
            return null
        }

        val authenticationPrincipal = authentication.getAttribute<Authentication>(PRINCIPAL_ATTRIBUTE_KEY)

        if (Objects.isNull(authenticationPrincipal)) {
            return null
        }

        val principalObject = authenticationPrincipal.principal

        if (LogicUtils.not(principalObject is DefaultPrincipal)) {
            return null
        }

        return principalObject as DefaultPrincipal
    }

    companion object {
        private val PRINCIPAL_ATTRIBUTE_KEY: String = Principal::class.java.name

        fun hasToken(authorization: OAuth2Authorization, token: String, @Nullable tokenType: OAuth2TokenType?): Boolean {
            if (tokenType == null) {
                return matchesState(authorization, token) ||
                        matchesAuthorizationCode(authorization, token) ||
                        matchesAccessToken(authorization, token) ||
                        matchesIdToken(authorization, token) ||
                        matchesRefreshToken(authorization, token) ||
                        matchesDeviceCode(authorization, token) ||
                        matchesUserCode(authorization, token)
            } else if (OAuth2ParameterNames.STATE == tokenType.value) {
                return matchesState(authorization, token)
            } else if (OAuth2ParameterNames.CODE == tokenType.value) {
                return matchesAuthorizationCode(authorization, token)
            } else if (OAuth2TokenType.ACCESS_TOKEN == tokenType) {
                return matchesAccessToken(authorization, token)
            } else if (OidcParameterNames.ID_TOKEN == tokenType.value) {
                return matchesIdToken(authorization, token)
            } else if (OAuth2TokenType.REFRESH_TOKEN == tokenType) {
                return matchesRefreshToken(authorization, token)
            } else if (OAuth2ParameterNames.DEVICE_CODE == tokenType.value) {
                return matchesDeviceCode(authorization, token)
            } else if (OAuth2ParameterNames.USER_CODE == tokenType.value) {
                return matchesUserCode(authorization, token)
            }
            return false
        }

        fun matchesState(authorization: OAuth2Authorization, token: String): Boolean {
            return token == authorization.getAttribute(OAuth2ParameterNames.STATE)
        }

        fun matchesAuthorizationCode(authorization: OAuth2Authorization, token: String): Boolean {
            val authorizationCode = authorization.getToken(OAuth2AuthorizationCode::class.java)
            return authorizationCode != null && authorizationCode.token.tokenValue == token
        }

        fun matchesAccessToken(authorization: OAuth2Authorization, token: String): Boolean {
            val accessToken =
                authorization.getToken(OAuth2AccessToken::class.java)
            return accessToken != null && accessToken.token.tokenValue == token
        }

        fun matchesRefreshToken(authorization: OAuth2Authorization, token: String): Boolean {
            val refreshToken =
                authorization.getToken(OAuth2RefreshToken::class.java)
            return refreshToken != null && refreshToken.token.tokenValue == token
        }

        fun matchesIdToken(authorization: OAuth2Authorization, token: String): Boolean {
            val idToken = authorization.getToken(OidcIdToken::class.java)
            return idToken != null && idToken.token.tokenValue == token
        }

        fun matchesDeviceCode(authorization: OAuth2Authorization, token: String): Boolean {
            val deviceCode =
                authorization.getToken(OAuth2DeviceCode::class.java)
            return deviceCode != null && deviceCode.token.tokenValue == token
        }

        fun matchesUserCode(authorization: OAuth2Authorization, token: String): Boolean {
            val userCode =
                authorization.getToken(OAuth2UserCode::class.java)
            return userCode != null && userCode.token.tokenValue == token
        }
    }
}
