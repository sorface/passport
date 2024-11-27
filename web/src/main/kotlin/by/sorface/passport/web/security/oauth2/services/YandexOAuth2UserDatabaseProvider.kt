package by.sorface.passport.web.security.oauth2.services

import by.sorface.passport.web.security.converters.PrincipalConverter
import by.sorface.passport.web.security.oauth2.converter.YandexOAuth2UserConverter
import by.sorface.passport.web.security.oauth2.records.YandexOAuth2User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class YandexOAuth2UserDatabaseProvider @Autowired constructor(
    yandexOAuth2UserService: YandexOAuth2UserService,
    principalConverter: PrincipalConverter,
    yandexOAuth2UserConverter: YandexOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<YandexOAuth2User>(yandexOAuth2UserService, principalConverter, yandexOAuth2UserConverter)