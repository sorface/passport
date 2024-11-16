package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.dao.models.enums.TokenOperationType
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.facade.emails.EmailLocaleMessageFacade
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest
import by.sorface.passport.web.services.tokens.TokenService
import by.sorface.passport.web.services.tokens.TokenValidator
import by.sorface.passport.web.services.users.UserService
import lombok.RequiredArgsConstructor
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * DefaultRenewPasswordFacade class is a service class that implements RenewPasswordFacade interface.
 * It provides methods for forgetting password, applying new password and checking renewal password token.
 */
@Service
@RequiredArgsConstructor
class DefaultRenewPasswordFacade(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val emailLocaleMessageFacade: EmailLocaleMessageFacade
) : RenewPasswordFacade {

    override fun forgetPassword(email: String?) {
        val user = userService!!.findByEmail(email)

        if (Objects.isNull(user)) {
            throw UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_EMAIL)
        }

        val token = tokenService!!.saveForUser(user, TokenOperationType.PASSWORD_RENEW)

        val locale = LocaleContextHolder.getLocale()

        emailLocaleMessageFacade!!.sendRenewPasswordEmail(locale, user!!.email, token.hash, user.username)
    }


    /**
     * applyNewPassword method is used to apply a new password.
     * It finds the token by hash, validates the token, encodes the new password, sets the new password to the user and saves the user.
     * It also deletes the token after the password is changed.
     *
     * @param request the request containing the new password and the hash of the token
     */
    @Transactional
    override fun applyNewPassword(request: ApplyNewPasswordRequest) {
        val token = tokenService!!.findByHash(request.hashToken)

        tokenValidator!!.validateOperation(token!!, TokenOperationType.PASSWORD_RENEW)
        tokenValidator.validateExpiredDate(token)

        val user: UserEntity = token.getUser()

        val encodedPassword = passwordEncoder!!.encode(request.newPassword)

        user.password = encodedPassword

        userService!!.save(user)

        tokenService.deleteByHash(token.getHash())
    }

    /**
     * checkRenewPasswordToken method is used to check a renewal password token.
     * It finds the token by hash and validates the token.
     *
     * @param hash the hash of the token
     */
    override fun checkRenewPasswordToken(hash: String?) {
        val token = tokenService!!.findByHash(hash)

        tokenValidator!!.validateOperation(token!!, TokenOperationType.PASSWORD_RENEW)
        tokenValidator.validateExpiredDate(token)
    }
}
