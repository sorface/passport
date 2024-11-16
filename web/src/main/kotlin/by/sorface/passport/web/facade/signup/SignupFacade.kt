package by.sorface.passport.web.facade.signup

import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ConfirmEmail
import by.sorface.passport.web.records.responses.UserConfirm
import by.sorface.passport.web.records.responses.UserRegisteredHash

/**
 *
 */
interface SignupFacade {

    fun signup(accountSignup: AccountSignup): UserRegisteredHash

    fun confirm(token: ConfirmEmail): UserConfirm

    fun findTokenByEmail(email: String?): UserRegisteredHash

}
