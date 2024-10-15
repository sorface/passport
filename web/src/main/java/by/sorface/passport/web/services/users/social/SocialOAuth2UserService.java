package by.sorface.passport.web.services.users.social;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.socialusers.SocialOAuth2User;

public interface SocialOAuth2UserService<T extends SocialOAuth2User> {

    UserEntity findOrCreate(final T oAuth2user);

}
