package by.sorface.passport.web.records.responses

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationClientRefreshSecret {
    var clientSecret: String? = null

    var expiresAt: LocalDateTime? = null
}
