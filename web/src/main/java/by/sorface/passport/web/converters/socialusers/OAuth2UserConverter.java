package by.sorface.passport.web.converters.socialusers;

import by.sorface.passport.web.records.socialusers.SocialOAuth2User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

public interface OAuth2UserConverter<T extends SocialOAuth2User> extends Converter<OAuth2User, T> {

    default <E> String getStringAttributeOrNull(final Map<String, E> oAuth2User, final String attributeName) {
        final Object value = oAuth2User.get(attributeName);

        if (Objects.isNull(value)) {
            return null;
        }

        if (value instanceof String valueString) {
            if ("null".equalsIgnoreCase(valueString.trim())) {
                return null;
            }

            return valueString;
        }

        return String.valueOf(value);
    }
}
