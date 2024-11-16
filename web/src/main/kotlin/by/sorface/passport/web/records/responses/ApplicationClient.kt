package by.sorface.passport.web.records.responses

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationClient {
    var id: String? = null

    var clientId: String? = null

    var clientSecret: String? = null

    var clientName: String? = null

    var issueTime: LocalDateTime? = null

    var expiresAt: LocalDateTime? = null

    var redirectUrls: Set<String>? = null
}
