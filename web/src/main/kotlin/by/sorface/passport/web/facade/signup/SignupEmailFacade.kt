package by.sorface.passport.web.facade.signup

import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ResendConfirmEmail
import by.sorface.passport.web.records.responses.UserRegistered

/**
 *
 */
interface SignupEmailFacade {

    fun signup(user: AccountSignup): UserRegistered

    fun resendConfirmEmail(email: ResendConfirmEmail): UserRegistered

}
