package by.sorface.passport.web.services.users.social;

import by.sorface.passport.web.dao.models.RoleEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.ProviderType;
import by.sorface.passport.web.records.socialusers.SocialOAuth2User;
import by.sorface.passport.web.services.users.RoleService;
import by.sorface.passport.web.services.users.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

public abstract class AbstractSocialOAuth2UserService<T extends SocialOAuth2User> implements SocialOAuth2UserService<T> {

    private static final String DEFAULT_ROLE_USER = "User";

    private final ProviderType providerType;

    private final UserService userService;

    private final RoleService roleService;

    protected AbstractSocialOAuth2UserService(final ProviderType providerType,
                                              final UserService userService,
                                              final RoleService roleService) {
        this.providerType = providerType;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public UserEntity findOrCreate(final T socialOAuth2User) {
        final UserEntity user = userService.findByProviderAndExternalId(providerType, socialOAuth2User.getId());

        if (Objects.nonNull(user)) {
            return user;
        }

        final UserEntity newUser = this.createNewUser(socialOAuth2User);

        final RoleEntity userRole = roleService.findByValue(DEFAULT_ROLE_USER);
        newUser.setRoles(List.of(userRole));

        return userService.save(newUser);
    }

    protected UserEntity createNewUser(final T socialOAuth2User) {
        final var newUser = new UserEntity();
        {
            newUser.setUsername(buildUserName(socialOAuth2User.getUsername(), socialOAuth2User.getId()));
            newUser.setAvatarUrl(socialOAuth2User.getAvatarUrl());
            newUser.setLastName(socialOAuth2User.getLastName());
            newUser.setFirstName(socialOAuth2User.getFirstName());
            newUser.setMiddleName(socialOAuth2User.getMiddleName());
            newUser.setExternalId(socialOAuth2User.getId());
            newUser.setProviderType(providerType);
            newUser.setAvatarUrl(socialOAuth2User.getAvatarUrl());
        }

        return newUser;
    }

    private String buildUserName(final String username, final String id) {
        boolean existUsername = userService.isExistUsername(username);

        return existUsername
                ? username + "_" + id.replaceAll("-", "").substring(4)
                : username;
    }

}
