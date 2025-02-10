package by.devpav.kotlin.oidcidp.service

import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdvancedSecurityEvaluator {

    fun hasPrincipalId(id: UUID): Boolean = SecurityContextHolder.getContext().getPrincipalIdOrNull() == id

}