package by.sorface.passport.web.converters.socialusers;

import by.sorface.passport.web.constants.claims.YandexClaims;
import by.sorface.passport.web.records.socialusers.YandexOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class YandexOAuth2UserConverter implements OAuth2UserConverter<YandexOAuth2User> {

    @Override
    public YandexOAuth2User convert(final OAuth2User oAuth2User) {
        final Map<String, Object> attributes = oAuth2User.getAttributes();

        final var id = getStringAttributeOrNull(attributes, YandexClaims.CLAIM_ID);
        final var login = getStringAttributeOrNull(attributes, YandexClaims.CLAIM_LOGIN);
        final Object avatarUrlObject = getStringAttributeOrNull(attributes, YandexClaims.CLAIM_AVATAR_URL);

        final var yandexOAuth2UserBuilder = YandexOAuth2User.builder()
                .id(id)
                .username(login);

        if (Objects.nonNull(avatarUrlObject)) {
            yandexOAuth2UserBuilder.avatarUrl(String.valueOf(avatarUrlObject));
        }

        final var email = getStringAttributeOrNull(attributes, YandexClaims.CLAIM_DEFAULT_EMAIL);
        yandexOAuth2UserBuilder.email(email);

        return yandexOAuth2UserBuilder.build();
    }

}
