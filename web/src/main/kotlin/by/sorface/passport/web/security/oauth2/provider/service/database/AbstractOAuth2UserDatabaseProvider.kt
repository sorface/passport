package by.sorface.passport.web.security.oauth2.provider.service.database

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.security.extensions.OAuth2UserConverter
import by.sorface.passport.web.security.oauth2.provider.service.SocialOAuth2UserService
import by.sorface.passport.web.security.oauth2.records.ExternalOAuth2User
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.transaction.annotation.Transactional

abstract class AbstractOAuth2UserDatabaseProvider<T : ExternalOAuth2User>(
    private val oAuth2UserSocialOAuth2UserService: SocialOAuth2UserService<T>,
    private val principalConverter: Converter<UserEntity, DefaultPrincipal>,
    private val oAuth2UserConverter: OAuth2UserConverter<T>
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val userEntity = this.getOrCreate(oAuth2User)

        return principalConverter.convert(userEntity) as OAuth2User
    }

    private fun getOrCreate(oAuth2User: OAuth2User): UserEntity {
        val auth2User = oAuth2UserConverter.convert(oAuth2User)

        return oAuth2UserSocialOAuth2UserService.findOrCreate(auth2User!!)
    }
}