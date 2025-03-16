package by.sorface.idp.config.security.evaluators

import by.sorface.idp.extencions.getPrincipalIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdvancedSecurityEvaluator {

    fun hasPrincipalId(id: UUID): Boolean = SecurityContextHolder.getContext().getPrincipalIdOrNull() == id

}