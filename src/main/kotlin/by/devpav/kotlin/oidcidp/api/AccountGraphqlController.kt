package by.devpav.kotlin.oidcidp.api

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrNull
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrThrow
import by.devpav.kotlin.oidcidp.graphql.*
import by.devpav.kotlin.oidcidp.service.AccountService
import by.devpav.kotlin.oidcidp.records.I18Codes
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class AccountGraphqlController(private val accountService: AccountService) {

    @QueryMapping
    fun accountGetAuthorized(): UserModel? {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(
            GraphqlUserException(I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED)
        )

        return accountService.findById(principalId)
    }

    @QueryMapping
    fun accountGetById(@Argument id: UUID): UserModel? = accountService.findById(id);

    @QueryMapping
    fun accountGetByUsername(@Argument username: String): UserModel? = accountService.findByUsername(username)

    @QueryMapping
    fun accountIsAuthenticated(): AccountAuthenticated =
        AccountAuthenticated.builder()
            .setAccess(SecurityContextHolder.getContext().getPrincipalIdOrNull() != null)
            .build()

    @QueryMapping
    fun accountExistsByUsername(@Argument username: String): AccountExists =
        AccountExists.builder()
            .setExists(accountService.isExistByUsername(username))
            .build()

    @MutationMapping
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#account.id) || hasRole('ROLE_ADMIN')")
    fun accountUpdateDetails(@Argument account: PatchUpdateAccount): UserModel? = accountService.updateInformation(account)

    @MutationMapping
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#accountUsernameUpdate.id) || hasRole('ADMIN')")
    fun accountUpdateUsernameById(@Argument accountUsernameUpdate: AccountUsernameUpdate): AccountUsername =
        accountService.updateUsername(accountUsernameUpdate.id, accountUsernameUpdate.username)

}
