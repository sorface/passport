package by.sorface.passport.web.records.tokens

import java.net.URL
import java.time.Instant

class TokenRecord {
    var active: Boolean? = null

    var sub: String? = null

    var aud: List<String>? = null

    var nbf: Instant? = null

    var scopes: List<String>? = null

    var iss: URL? = null

    var exp: Instant? = null

    var iat: Instant? = null

    var jti: String? = null

    var clientId: String? = null

    var tokenType: String? = null

    var principal: IntrospectionPrincipal? = null
}
