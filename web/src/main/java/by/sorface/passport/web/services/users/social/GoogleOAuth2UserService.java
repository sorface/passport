package by.sorface.passport.web.services.users.social;

import by.sorface.passport.web.dao.models.enums.ProviderType;
import by.sorface.passport.web.records.socialusers.GoogleOAuth2User;
import by.sorface.passport.web.services.users.RoleService;
import by.sorface.passport.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserService extends AbstractSocialOAuth2UserService<GoogleOAuth2User>
        implements SocialOAuth2UserService<GoogleOAuth2User> {

    @Autowired
    protected GoogleOAuth2UserService(final UserService userService,
                                      final RoleService roleService) {
        super(ProviderType.GOOGLE, userService, roleService);
    }

}
