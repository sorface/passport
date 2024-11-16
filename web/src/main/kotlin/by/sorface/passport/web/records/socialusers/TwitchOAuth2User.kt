package by.sorface.passport.web.records.socialusers

import lombok.Builder
import lombok.Getter

@Getter
@Builder
class TwitchOAuth2User : SocialOAuth2User {
    override val id: String? = null

    override val username: String? = null

    override val email: String? = null

    override val firstName: String? = null

    override val lastName: String? = null

    override val middleName: String? = null

    override val avatarUrl: String? = null

    private val attributes: Map<String, Any>? = null
}
