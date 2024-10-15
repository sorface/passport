package by.sorface.passport.web.services.users.providers.socials;

import by.sorface.passport.web.converters.OAuth2TwitchUserRequestEntityConverter;
import by.sorface.passport.web.converters.PrincipalConverter;
import by.sorface.passport.web.converters.socialusers.TwitchOAuth2UserConverter;
import by.sorface.passport.web.records.socialusers.TwitchOAuth2User;
import by.sorface.passport.web.services.users.providers.AbstractOAuth2UserDatabaseProvider;
import by.sorface.passport.web.services.users.social.TwitchOAuth2UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwitchOAuth2UserDatabaseProvider extends AbstractOAuth2UserDatabaseProvider<TwitchOAuth2User> {

    public static final OAuth2TwitchUserRequestEntityConverter TWITCH_REQUEST_CONVERTER;

    static {
        TWITCH_REQUEST_CONVERTER = new OAuth2TwitchUserRequestEntityConverter();
    }

    @Autowired
    public TwitchOAuth2UserDatabaseProvider(final TwitchOAuth2UserService twitchOAuth2UserService,
                                            final PrincipalConverter principalConverter,
                                            final TwitchOAuth2UserConverter twitchOAuth2UserConverter) {
        super(twitchOAuth2UserService, principalConverter, twitchOAuth2UserConverter);
    }

    @PostConstruct
    void init() {
        super.setRequestEntityConverter(TWITCH_REQUEST_CONVERTER);
    }

}
