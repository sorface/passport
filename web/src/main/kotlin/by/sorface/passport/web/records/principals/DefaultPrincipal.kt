package by.sorface.passport.web.records.principals

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.LocalDate
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class DefaultPrincipal(username: String?, password: String?, enabled: Boolean, authorities: Collection<GrantedAuthority?>?) :
    User(username, password, enabled, true, true, true, authorities), OAuth2User, OidcUser {

    /**
     * Internal user id
     */
    var id: UUID? = null

    /**
     * firstname user
     */
    var firstName: String? = null

    /**
     * surname user
     */
    var lastName: String? = null

    /**
     * middleName user
     */
    var _middleName: String? = null

    /**
     * birthday of user
     */
    var birthday: LocalDate? = null

    /**
     * link to avatar of user
     */
    var avatarUrl: String? = null

    /**
     * email of user
     */
    var _email: String? = null

    /**
     * confirmed email
     */
    var confirm = false

    /**
     * Other users
     */
    var oauthAttributes: Map<String, Any>? = null

    override fun getAttributes(): Map<String, Any> {
        return oauthAttributes!!
    }

    override fun getName(): String {
        return this.username
    }

    override fun getClaims(): Map<String, Any> {
        return oauthAttributes!!
    }

    override fun getUserInfo(): OidcUserInfo {
        return OidcUserInfo.builder()
            .email(_email)
            .nickname(username)
            .picture(avatarUrl)
            .build()
    }

    override fun getIdToken(): OidcIdToken {
        return OidcIdToken.withTokenValue("value").build()
    }

    override fun getEmail(): String? {
        return this._email
    }

    override fun getMiddleName(): String? {
        return this._middleName
    }
}

