package by.sorface.passport.web.services.users.providers.socials

import by.sorface.passport.web.converters.PrincipalConverter
import by.sorface.passport.web.converters.socialusers.YandexOAuth2UserConverter
import by.sorface.passport.web.records.socialusers.YandexOAuth2User
import by.sorface.passport.web.services.users.providers.AbstractOAuth2UserDatabaseProvider
import by.sorface.passport.web.services.users.social.YandexOAuth2UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class YandexOAuth2UserDatabaseProvider @Autowired constructor(
    yandexOAuth2UserService: YandexOAuth2UserService,
    principalConverter: PrincipalConverter,
    yandexOAuth2UserConverter: YandexOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<YandexOAuth2User>(yandexOAuth2UserService, principalConverter, yandexOAuth2UserConverter)
