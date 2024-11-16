package by.sorface.passport.web.services.users.providers.socials

import by.sorface.passport.web.converters.PrincipalConverter
import by.sorface.passport.web.converters.socialusers.GoogleOAuth2UserConverter
import by.sorface.passport.web.records.socialusers.GoogleOAuth2User
import by.sorface.passport.web.services.users.providers.AbstractOAuth2UserDatabaseProvider
import by.sorface.passport.web.services.users.social.GoogleOAuth2UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GoogleOAuth2UserDatabaseProvider @Autowired constructor(
    googleOAuth2UserService: GoogleOAuth2UserService,
    principalConverter: PrincipalConverter,
    googleOAuth2UserConverter: GoogleOAuth2UserConverter
) : AbstractOAuth2UserDatabaseProvider<GoogleOAuth2User>(googleOAuth2UserService, principalConverter, googleOAuth2UserConverter)
