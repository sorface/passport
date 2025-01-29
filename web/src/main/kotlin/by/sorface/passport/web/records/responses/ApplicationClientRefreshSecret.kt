package by.sorface.passport.web.records.responses

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationClientRefreshSecret {
    var clientSecret: String? = null

    var expiresAt: LocalDateTime? = null
}
