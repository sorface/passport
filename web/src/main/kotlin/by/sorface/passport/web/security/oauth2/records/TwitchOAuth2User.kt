package by.sorface.passport.web.security.oauth2.records

class TwitchOAuth2User : ExternalOAuth2User {
    override var id: String? = null

    override var username: String? = null

    override var email: String? = null

    override var firstName: String? = null

    override var lastName: String? = null

    override var middleName: String? = null

    override var avatarUrl: String? = null

    private var attributes: Map<String, Any>? = null
}