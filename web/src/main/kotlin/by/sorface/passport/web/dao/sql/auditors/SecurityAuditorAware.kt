package by.sorface.passport.web.dao.sql.auditors

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.security.extensions.getPrincipalIdOrNull
import by.sorface.passport.web.services.users.UserService
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SecurityAuditorAware(private val userService: UserService) : AuditorAware<UserEntity> {

    override fun getCurrentAuditor(): Optional<UserEntity> {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrNull()
            ?: return Optional.empty()

        return Optional.ofNullable(userService.findById(principalId))
    }
}
