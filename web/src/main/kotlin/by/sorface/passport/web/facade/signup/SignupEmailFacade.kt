package by.sorface.passport.web.facade.signup

import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ResendConfirmEmail
import by.sorface.passport.web.records.responses.UserRegistered

/**
 *
 */
interface SignupEmailFacade {
    /**
     * The process user registration in the system
     *
     * @param user данные о пользователе
     * @return data of the registered user
     */
    fun signup(user: AccountSignup): UserRegistered

    /**
     * Resend confirm mail to user's email
     *
     * @param email user's email
     * @return info by confirmation email
     */
    fun resendConfirmEmail(email: ResendConfirmEmail): UserRegistered
}
