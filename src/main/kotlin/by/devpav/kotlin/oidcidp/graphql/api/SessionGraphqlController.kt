package by.devpav.kotlin.oidcidp.graphql.api

import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.extencions.getPrincipalOrThrow
import by.devpav.kotlin.oidcidp.graphql.Session
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.graphql.services.SessionService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller

@Controller
class SessionGraphqlController(private val sessionService: SessionService) {

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    fun sessionGetAllByUser() : List<Session> {
        val principal = SecurityContextHolder.getContext().getPrincipalOrThrow(
            GraphqlUserException(I18Codes.I18GlobalCodes.ACCESS_DENIED)
        )

        return sessionService.getAllByUsername(principal.username)
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun sessionGetAllByUsername(@Argument username: String) : List<Session> = sessionService.getAllByUsername(username)

}