package by.devpav.kotlin.oidcidp.service

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2UserDatabaseStrategy : OAuth2UserService<OAuth2UserRequest, OAuth2User>
