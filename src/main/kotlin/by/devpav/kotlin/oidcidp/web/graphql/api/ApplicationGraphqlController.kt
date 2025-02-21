package by.devpav.kotlin.oidcidp.web.graphql.api

import by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel
import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrThrow
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.web.graphql.services.ApplicationService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller

@Controller
class ApplicationGraphqlController(private val applicationService: ApplicationService) {

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun applicationGetAll(): List<RegisteredClientModel> = applicationService.getAll()

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    fun applicationGetByUser(): List<RegisteredClientModel> {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(
            GraphqlUserException(I18Codes.I18GlobalCodes.ACCESS_DENIED)
        )

        return applicationService.getAllByUser(principalId)
    }

}