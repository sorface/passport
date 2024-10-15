package by.sorface.passport.web.services.users.providers;

import by.sorface.passport.web.constants.OAuthProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class DefaultOAuth2UserDatabaseStrategy implements OAuth2UserDatabaseStrategy {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> gitHubOAuth2UserSocialOAuth2UserService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> yandexOAuth2UserSocialOAuth2UserService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> twitchOAuth2UserSocialOAuth2UserService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> googleOAuth2UserSocialOAuth2UserService;

    public DefaultOAuth2UserDatabaseStrategy(@Qualifier("gitHubOAuth2UserDatabaseProvider") final OAuth2UserService<OAuth2UserRequest, OAuth2User> gitHubOAuth2UserSocialOAuth2UserService,
                                             @Qualifier("yandexOAuth2UserDatabaseProvider") final OAuth2UserService<OAuth2UserRequest, OAuth2User> yandexOAuth2UserSocialOAuth2UserService,
                                             @Qualifier("twitchOAuth2UserDatabaseProvider") final OAuth2UserService<OAuth2UserRequest, OAuth2User> twitchOAuth2UserSocialOAuth2UserService,
                                             @Qualifier("googleOAuth2UserDatabaseProvider") final OAuth2UserService<OAuth2UserRequest, OAuth2User> googleOAuth2UserSocialOAuth2UserService) {
        this.gitHubOAuth2UserSocialOAuth2UserService = gitHubOAuth2UserSocialOAuth2UserService;
        this.yandexOAuth2UserSocialOAuth2UserService = yandexOAuth2UserSocialOAuth2UserService;
        this.twitchOAuth2UserSocialOAuth2UserService = twitchOAuth2UserSocialOAuth2UserService;
        this.googleOAuth2UserSocialOAuth2UserService = googleOAuth2UserSocialOAuth2UserService;
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final var provider = getAuthProvider(userRequest);

        final var providerService = getUserServiceByProvider(provider);

        return providerService.loadUser(userRequest);
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> getUserServiceByProvider(OAuthProvider provider) {
        return switch (provider) {
            case GITHUB -> gitHubOAuth2UserSocialOAuth2UserService;
            case YANDEX -> yandexOAuth2UserSocialOAuth2UserService;
            case TWITCH -> twitchOAuth2UserSocialOAuth2UserService;
            case GOOGLE -> googleOAuth2UserSocialOAuth2UserService;

            default -> throw new OAuth2AuthenticationException("Provider %s not supported".formatted(provider));
        };
    }

    private OAuthProvider getAuthProvider(final OAuth2UserRequest oAuth2UserRequest) {
        final String providerName = oAuth2UserRequest.getClientRegistration().getClientName();

        return OAuthProvider.findByName(providerName);
    }


}
