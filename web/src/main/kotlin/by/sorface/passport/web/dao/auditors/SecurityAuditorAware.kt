package by.sorface.passport.web.dao.auditors

import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.services.users.UserService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
@RequiredArgsConstructor
class SecurityAuditorAware(private val userService: UserService) : AuditorAware<UserEntity> {

    override fun getCurrentAuditor(): Optional<UserEntity> {
        val authentication = SecurityContextHolder.getContext().authentication

        if (Objects.isNull(authentication) || !authentication.isAuthenticated || (authentication is AnonymousAuthenticationToken)) {
            return Optional.empty()
        }

        val principal = authentication.principal as DefaultPrincipal

        return Optional.ofNullable(userService.findById(principal.id))
    }
}
