package by.devpav.kotlin.oidcidp.service.oauth.providers

import by.devpav.kotlin.oidcidp.config.security.oauth2.records.ExternalOAuth2User
import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import by.devpav.kotlin.oidcidp.service.oauth.SocialOAuth2UserService
import by.devpav.kotlin.oidcidp.service.oauth.converters.OAuth2UserConverter
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.transaction.annotation.Transactional

abstract class AbstractOAuth2UserDatabaseProvider<T : ExternalOAuth2User>(
    private val oAuth2UserSocialOAuth2UserService: SocialOAuth2UserService<T>,
    private val principalConverter: Converter<UserModel, SorfacePrincipal>,
    private val oAuth2UserConverter: OAuth2UserConverter<T>
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val userEntity = this.getOrCreate(oAuth2User)

        return principalConverter.convert(userEntity) as OAuth2User
    }

    private fun getOrCreate(oAuth2User: OAuth2User): UserModel {
        val auth2User = oAuth2UserConverter.convert(oAuth2User)

        return oAuth2UserSocialOAuth2UserService.findOrCreate(auth2User!!)
    }
}