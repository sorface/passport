package by.sorface.passport.web.security.extensions

import by.sorface.passport.web.records.principals.DefaultPrincipal
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import java.util.*

fun SecurityContext.getPrincipal(): DefaultPrincipal? {
    val auth: Authentication? = this.authentication

    if (auth == null || auth.isAuthenticated.not() || auth is AnonymousAuthenticationToken) {
        return null
    }

    return auth.principal as DefaultPrincipal?
}

fun <T : RuntimeException> SecurityContext.getPrincipalOrThrow(throwable: T): DefaultPrincipal = this.getPrincipal() ?: throw throwable

fun SecurityContext.getPrincipalIdOrNull(): UUID? = this.getPrincipal()?.id

fun <T : RuntimeException> SecurityContext.getPrincipalIdOrThrow(throwable: T): UUID = this.getPrincipalIdOrNull() ?: throw throwable
