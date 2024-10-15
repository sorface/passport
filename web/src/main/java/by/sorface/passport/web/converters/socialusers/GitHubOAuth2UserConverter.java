package by.sorface.passport.web.converters.socialusers;

import by.sorface.passport.web.constants.claims.GitHubClaims;
import by.sorface.passport.web.records.socialusers.GitHubOAuth2User;
import by.sorface.passport.web.utils.UserUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class GitHubOAuth2UserConverter implements OAuth2UserConverter<GitHubOAuth2User> {

    @Override
    public GitHubOAuth2User convert(final OAuth2User oAuth2User) {
        final Map<String, Object> attributes = oAuth2User.getAttributes();

        final var id = getStringAttributeOrNull(attributes, GitHubClaims.CLAIM_ID);
        final var avatarUrl = getStringAttributeOrNull(attributes, GitHubClaims.CLAIM_AVATAR_URL);
        final var login = getStringAttributeOrNull(attributes, GitHubClaims.CLAIM_LOGIN);
        final var name = getStringAttributeOrNull(attributes, GitHubClaims.CLAIM_NAME);
        final var email = getStringAttributeOrNull(attributes, GitHubClaims.CLAIM_EMAIL);

        final var builder = GitHubOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(login)
                .email(email);

        if (Objects.nonNull(name)) {
            final UserUtils.DefaultFullName defaultFullName = UserUtils.parseFullName(name);

            builder
                    .firstName(defaultFullName.getFirstName())
                    .lastName(defaultFullName.getLastName())
                    .middleName(defaultFullName.getOtherName());
        }

        return builder.build();
    }

}
