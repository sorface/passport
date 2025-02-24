package by.devpav.kotlin.oidcidp.web.graphql.api

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrNull
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrThrow
import by.devpav.kotlin.oidcidp.graphql.*
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.web.graphql.services.impl.DefaultAccountService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class AccountGraphqlController(private val defaultAccountService: DefaultAccountService) {

    @QueryMapping
    fun accountGetAuthorized(): UserModel? {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(
            AccessDeniedException(I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED)
        )

        return defaultAccountService.findById(principalId)
    }

    @QueryMapping
    fun accountGetById(@Argument id: UUID): UserModel? = defaultAccountService.findById(id)

    @QueryMapping
    fun accountGetByUsername(@Argument username: String): UserModel? = defaultAccountService.findByUsername(username)

    @QueryMapping
    fun accountIsAuthenticated(): AccountAuthenticated = AccountAuthenticated
        .builder()
        .setAccess(SecurityContextHolder.getContext().getPrincipalIdOrNull() != null)
        .build()

    @QueryMapping
    fun accountExistsByUsername(@Argument username: String): AccountExists = AccountExists.builder().setExists(defaultAccountService.isExistByUsername(username)).build()

    @MutationMapping
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#account.id) || hasRole('ROLE_ADMIN')")
    fun accountUpdateDetails(@Argument account: PatchUpdateAccount): UserModel? = defaultAccountService.updateInformation(account)

    @MutationMapping
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#accountUsernameUpdate.id) || hasRole('ADMIN')")
    fun accountUpdateUsernameById(@Argument accountUsernameUpdate: AccountUsernameUpdate): AccountUsername =
        defaultAccountService.updateUsername(accountUsernameUpdate.id, accountUsernameUpdate.username)

}
