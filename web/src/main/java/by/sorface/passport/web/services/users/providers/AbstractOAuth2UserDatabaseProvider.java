package by.sorface.passport.web.services.users.providers;

import by.sorface.passport.web.converters.socialusers.OAuth2UserConverter;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.records.socialusers.SocialOAuth2User;
import by.sorface.passport.web.services.users.social.SocialOAuth2UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for getting a user from third-party services
 */
public abstract class AbstractOAuth2UserDatabaseProvider<T extends SocialOAuth2User> extends DefaultOAuth2UserService {

    private final SocialOAuth2UserService<T> oAuth2UserSocialOAuth2UserService;

    private final Converter<UserEntity, DefaultPrincipal> principalConverter;

    private final OAuth2UserConverter<T> oAuth2UserConverter;

    public AbstractOAuth2UserDatabaseProvider(final SocialOAuth2UserService<T> oAuth2UserSocialOAuth2UserService,
                                              final Converter<UserEntity, DefaultPrincipal> principalConverter,
                                              final OAuth2UserConverter<T> oAuth2UserConverter) {
        this.oAuth2UserSocialOAuth2UserService = oAuth2UserSocialOAuth2UserService;
        this.principalConverter = principalConverter;
        this.oAuth2UserConverter = oAuth2UserConverter;
    }

    /**
     * Getting user data from third-party services
     *
     * @param userRequest the user request
     * @return пользователь oauth 2.0
     * @throws OAuth2AuthenticationException error getting the user
     */
    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final var oAuth2User = super.loadUser(userRequest);

        final var userEntity = this.getOrCreate(oAuth2User);

        return principalConverter.convert(userEntity);
    }

    private UserEntity getOrCreate(final OAuth2User oAuth2User) {
        final T auth2User = oAuth2UserConverter.convert(oAuth2User);

        return oAuth2UserSocialOAuth2UserService.findOrCreate(auth2User);
    }

}
