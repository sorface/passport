package by.sorface.passport.web.services.users.providers;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserDatabaseStrategy extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
}
