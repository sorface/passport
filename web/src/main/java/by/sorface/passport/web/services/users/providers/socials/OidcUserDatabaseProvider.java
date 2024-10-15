package by.sorface.passport.web.services.users.providers.socials;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class OidcUserDatabaseProvider {

    private final UserService userService;

    public OidcUserInfo loadUser(final String username, final Set<String> scopes) {
        final OidcUserInfo.Builder builder = OidcUserInfo.builder().subject(username);

        if (scopes.isEmpty()) {
            return builder.build();
        }

        final UserEntity user = userService.findByUsername(username);


        if (scopes.contains(OidcScopes.PROFILE)) {
            return builder.nickname(username)
                    .email(user.getEmail())
                    .picture(user.getAvatarUrl())
                    .emailVerified(user.isConfirm())
                    .build();
        }

        if (scopes.contains(OidcScopes.EMAIL)) {
            return builder.email(user.getEmail()).build();
        }

        return builder.build();
    }

}
