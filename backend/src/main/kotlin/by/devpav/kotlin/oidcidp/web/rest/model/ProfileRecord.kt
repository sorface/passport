package by.devpav.kotlin.oidcidp.web.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProfileRecord(
    val id: UUID?,
    val nickname: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val avatar: String?,
    val roles: List<String>
)
