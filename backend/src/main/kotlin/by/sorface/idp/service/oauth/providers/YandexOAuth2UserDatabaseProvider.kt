package by.sorface.idp.service.oauth.providers

import by.sorface.idp.config.security.oauth2.records.YandexOAuth2User
import by.sorface.idp.service.oauth.jdbc.YandexOAuth2UserService
import by.sorface.idp.service.oauth.converters.PrincipalConverter
import by.sorface.idp.service.oauth.converters.YandexOAuth2UserConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class YandexOAuth2UserDatabaseProvider @Autowired constructor(
    yandexOAuth2UserService: YandexOAuth2UserService,
    principalConverter: PrincipalConverter,
    yandexOAuth2UserConverter: YandexOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<YandexOAuth2User>(yandexOAuth2UserService, principalConverter, yandexOAuth2UserConverter)