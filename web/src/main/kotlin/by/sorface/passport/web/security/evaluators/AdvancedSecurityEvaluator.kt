package by.sorface.passport.web.security.evaluators

import by.sorface.passport.web.security.extensions.getPrincipalIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdvancedSecurityEvaluator {

    fun hasUserId(id: UUID): Boolean = SecurityContextHolder.getContext().getPrincipalIdOrNull() == id

}