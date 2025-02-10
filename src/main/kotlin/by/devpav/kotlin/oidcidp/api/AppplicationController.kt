package by.devpav.kotlin.oidcidp.api

import by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.client.JdbcRegisteredClientRepository
import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrThrow
import by.devpav.kotlin.oidcidp.records.I18Codes
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller

@Controller
class AppplicationController(private val jdbcRegisteredClientRepository: JdbcRegisteredClientRepository) {

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun applicationGetAll(): MutableList<RegisteredClientModel> = jdbcRegisteredClientRepository.findAll()

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    fun applicationGetByUser(): MutableList<RegisteredClientModel> {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(
            GraphqlUserException(I18Codes.I18GlobalCodes.ACCESS_DENIED)
        )

        return jdbcRegisteredClientRepository.findByCreatedUser(principalId)
    }

}