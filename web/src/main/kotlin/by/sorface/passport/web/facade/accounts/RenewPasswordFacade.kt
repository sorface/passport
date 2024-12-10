package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest

interface RenewPasswordFacade {
    fun forgetPassword(email: String)

    fun applyNewPassword(request: ApplyNewPasswordRequest)

    fun checkRenewPasswordToken(hash: String)
}
