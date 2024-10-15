package by.sorface.passport.web.services.users.social;

import by.sorface.passport.web.dao.models.enums.ProviderType;
import by.sorface.passport.web.records.socialusers.GitHubOAuth2User;
import by.sorface.passport.web.services.users.RoleService;
import by.sorface.passport.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserService extends AbstractSocialOAuth2UserService<GitHubOAuth2User>
        implements SocialOAuth2UserService<GitHubOAuth2User> {

    @Autowired
    public GitHubOAuth2UserService(final UserService userService,
                                   final RoleService roleService) {
        super(ProviderType.GITHUB, userService, roleService);
    }

}
