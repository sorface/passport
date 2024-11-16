package by.sorface.passport.web.records.socialusers

interface SocialOAuth2User {
    val id: String?

    val username: String?

    val email: String?

    val firstName: String?

    val lastName: String?

    val middleName: String?

    val avatarUrl: String?
}
