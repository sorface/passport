package by.devpav.kotlin.oidcidp.extencions

import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import java.util.*

fun SecurityContext.getPrincipal(): SorfacePrincipal? {
    val auth: Authentication? = this.authentication

    if (auth == null || auth.isAuthenticated.not() || auth is AnonymousAuthenticationToken) {
        return null
    }

    return auth.principal as SorfacePrincipal?
}

fun <T : RuntimeException> SecurityContext.getPrincipalOrThrow(throwable: T): SorfacePrincipal = this.getPrincipal() ?: throw throwable

fun SecurityContext.getPrincipalIdOrNull(): UUID? = this.getPrincipal()?.id

fun <T : RuntimeException> SecurityContext.getPrincipalIdOrThrow(throwable: T): UUID = this.getPrincipalIdOrNull() ?: throw throwable
