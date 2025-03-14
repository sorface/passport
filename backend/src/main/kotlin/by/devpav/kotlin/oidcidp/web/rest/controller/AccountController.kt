package by.devpav.kotlin.oidcidp.web.rest.controller

import by.devpav.kotlin.oidcidp.web.rest.facade.AccountFacade
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val accountFacade: AccountFacade) {

    @GetMapping(value = ["/authenticated"])
    fun isAuthorizedUser(): AccountAuthenticated = accountFacade.isAuthenticated()

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = ["/current"])
    fun getCurrentAuthorizedUser(): Account = accountFacade.getCurrentAuthorized()

    @PatchMapping("/{id}")
    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#id) or hasAnyRole('ADMIN')")
    fun update(@RequestBody userPatchUpdate: AccountPatchUpdate, @PathVariable id: UUID): Account = accountFacade.update(id, userPatchUpdate)

    @PreAuthorize("@advancedSecurityEvaluator.hasPrincipalId(#id) or hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/username")
    fun updateUsername(@PathVariable("id") id: UUID, @RequestBody @Valid request: AccountUsernameUpdate): Account =
        accountFacade.updateUsername(id, request)

    @GetMapping("/username/{username}/exists")
    fun isExistsUsername(@PathVariable("username") @NotNull username: String?): AccountExistsResponse =
        accountFacade.isExistsByUsername(username!!)

    @GetMapping("/login/{login}/exists")
    fun isExistsByUsernameOrEmail(@PathVariable("login") @NotNull username: String): AccountExistsResponse =
        accountFacade.isExistsByUsernameOrEmail(username)

}
