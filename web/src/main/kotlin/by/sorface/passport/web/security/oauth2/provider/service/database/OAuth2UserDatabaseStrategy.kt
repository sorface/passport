package by.sorface.passport.web.security.oauth2.provider.service.database

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2UserDatabaseStrategy : OAuth2UserService<OAuth2UserRequest, OAuth2User>
