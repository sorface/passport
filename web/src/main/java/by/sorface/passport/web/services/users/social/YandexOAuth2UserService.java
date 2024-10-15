package by.sorface.passport.web.services.users.social;

import by.sorface.passport.web.dao.models.enums.ProviderType;
import by.sorface.passport.web.records.socialusers.YandexOAuth2User;
import by.sorface.passport.web.services.users.RoleService;
import by.sorface.passport.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YandexOAuth2UserService extends AbstractSocialOAuth2UserService<YandexOAuth2User>
        implements SocialOAuth2UserService<YandexOAuth2User> {

    @Autowired
    protected YandexOAuth2UserService(final UserService userService,
                                      final RoleService roleService) {
        super(ProviderType.YANDEX, userService, roleService);
    }

}
