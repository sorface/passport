package by.sorface.passport.web.records.principals

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.util.*

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class OAuth2Session : Serializable {
    private val principleId: UUID? = null

    private val authorizationId: String? = null

    private val initiatorSystem: String? = null
}
