package by.devpav.kotlin.oidcidp.dao.sql.auditors

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SecurityAuditorAware(private val userRepository: UserRepository) : AuditorAware<UserModel> {

    override fun getCurrentAuditor(): Optional<UserModel> {
        val principalId = (SecurityContextHolder.getContext().authentication.principal as SorfacePrincipal).id

        principalId ?: return Optional.empty()

        return userRepository.findById(principalId)
    }
}
