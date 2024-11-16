package by.sorface.passport.web.controllers

import by.sorface.passport.web.facade.accounts.RenewPasswordFacade
import by.sorface.passport.web.records.accounts.AccountRenewPasswordRequest
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts/passwords")
class RenewPasswordController {
    private val renewPasswordFacade: RenewPasswordFacade? = null

    @PostMapping("/forget")
    fun forgetPassword(@RequestBody request: @Valid AccountRenewPasswordRequest?): ResponseEntity<*> {
        renewPasswordFacade!!.forgetPassword(request!!.email)

        return ResponseEntity.ok().build<Any>()
    }

    @PostMapping("/new/apply")
    fun applyNewPassword(@RequestBody request: @Valid ApplyNewPasswordRequest?): ResponseEntity<*> {
        renewPasswordFacade!!.applyNewPassword(request!!)

        return ResponseEntity.ok().build<Any>()
    }

    @GetMapping("/hash/introspect")
    fun checkRenewPasswordToken(@RequestParam("hash") hash: String?): ResponseEntity<*> {
        renewPasswordFacade!!.checkRenewPasswordToken(hash)

        return ResponseEntity.ok().build<Any>()
    }
}
