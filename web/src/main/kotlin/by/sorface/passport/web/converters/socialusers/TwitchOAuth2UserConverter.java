package by.sorface.passport.web.converters.socialusers;

import by.sorface.passport.web.constants.claims.TwitchClaims;
import by.sorface.passport.web.exceptions.ObjectInvalidException;
import by.sorface.passport.web.records.socialusers.TwitchOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class TwitchOAuth2UserConverter implements OAuth2UserConverter<TwitchOAuth2User> {

    @Override
    @SuppressWarnings("unchecked")
    public TwitchOAuth2User convert(final OAuth2User oAuth2User) {
        final var user = ((ArrayList<Map<String, String>>) oAuth2User.getAttributes().get(TwitchClaims.CLAIM_CONTAINER))
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectInvalidException("twitch claims invalid"));

        final var id = getStringAttributeOrNull(user, TwitchClaims.CLAIM_ID);
        final var login = getStringAttributeOrNull(user, TwitchClaims.CLAIM_LOGIN);
        final var email = getStringAttributeOrNull(user, TwitchClaims.CLAIM_EMAIL);
        final var avatarUrl = getStringAttributeOrNull(user, TwitchClaims.CLAIM_PROFILE_IMAGE_URL);

        return TwitchOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(login)
                .email(email)
                .build();
    }

}
