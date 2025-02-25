package by.devpav.kotlin.oidcidp.config.security.oauth2.records

interface ExternalOAuth2User {
    var id: String?

    var username: String?

    var email: String?

    var firstName: String?

    var lastName: String?

    var middleName: String?

    var avatarUrl: String?
}
