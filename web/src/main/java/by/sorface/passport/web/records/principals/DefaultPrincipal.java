package by.sorface.passport.web.records.principals;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultPrincipal extends User implements OAuth2User, OidcUser {

    /**
     * Internal user id
     */
    private UUID id;

    /**
     * firstname user
     */
    private String firstName;

    /**
     * surname user
     */
    private String lastName;

    /**
     * middleName user
     */
    private String middleName;

    /**
     * birthday of user
     */
    private LocalDate birthday;

    /**
     * link to avatar of user
     */
    private String avatarUrl;

    /**
     * email of user
     */
    private String email;

    /**
     * confirmed email
     */
    private boolean confirm;

    /**
     * Other users
     */
    private Map<String, Object> oauthAttributes;

    public DefaultPrincipal(final String username, final String password, final boolean enabled, final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthAttributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oauthAttributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return OidcUserInfo.builder()
                .email(email)
                .nickname(getUsername())
                .picture(avatarUrl)
                .build();
    }

    @Override
    public OidcIdToken getIdToken() {
        return OidcIdToken.withTokenValue("value").build();
    }
}

