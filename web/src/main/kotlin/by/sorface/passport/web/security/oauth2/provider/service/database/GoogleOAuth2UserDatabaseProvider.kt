package by.sorface.passport.web.security.oauth2.provider.service.database

import by.sorface.passport.web.security.converters.PrincipalConverter
import by.sorface.passport.web.security.oauth2.provider.converter.GoogleOAuth2UserConverter
import by.sorface.passport.web.security.oauth2.provider.service.GoogleOAuth2UserService
import by.sorface.passport.web.security.oauth2.records.GoogleOAuth2User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class GoogleOAuth2UserDatabaseProvider @Autowired constructor(
    googleOAuth2UserService: GoogleOAuth2UserService,
    principalConverter: PrincipalConverter,
    googleOAuth2UserConverter: GoogleOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<GoogleOAuth2User>(googleOAuth2UserService, principalConverter, googleOAuth2UserConverter)