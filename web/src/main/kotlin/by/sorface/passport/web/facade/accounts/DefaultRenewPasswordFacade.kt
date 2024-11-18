package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.TokenOperationType
import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.extensions.hasNotOperation
import by.sorface.passport.web.extensions.hasOperation
import by.sorface.passport.web.extensions.isExpired
import by.sorface.passport.web.facade.emails.EmailLocaleMessageFacade
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest
import by.sorface.passport.web.services.tokens.TokenService
import by.sorface.passport.web.services.users.UserService
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * DefaultRenewPasswordFacade class is a service class that implements RenewPasswordFacade interface.
 * It provides methods for forgetting password, applying new password and checking renewal password token.
 */
@Service
open class DefaultRenewPasswordFacade(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val emailLocaleMessageFacade: EmailLocaleMessageFacade
) : RenewPasswordFacade {

    override fun forgetPassword(email: String) {
        val user = userService.findByEmail(email) ?: throw UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_EMAIL)

        val token = tokenService.saveForUser(user, TokenOperationType.PASSWORD_RENEW)

        val locale = LocaleContextHolder.getLocale()

        emailLocaleMessageFacade.sendRenewPasswordEmail(locale, user.email!!, token.hash, user.username)
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
        val token = tokenService.findByHash(request.hashToken) ?: throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)

        val user: UserEntity = token.user ?: throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)

        if (token.hasNotOperation(TokenOperationType.PASSWORD_RENEW)) {
            throw NotFoundException(I18Codes.I18TokenCodes.INVALID_OPERATION_TYPE)
        }

        if (token.isExpired()) {
            throw NotFoundException(I18Codes.I18TokenCodes.EXPIRED)
        }

        val encodedPassword = passwordEncoder.encode(request.newPassword)

        user.password = encodedPassword

        userService.save(user)

        tokenService.deleteByHash(token.hash)
    }

    /**
     * checkRenewPasswordToken method is used to check a renewal password token.
     * It finds the token by hash and validates the token.
     *
     * @param hash the hash of the token
     */
    override fun checkRenewPasswordToken(hash: String) {
        val token = tokenService.findByHash(hash) ?: throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)

        if (token.hasOperation(TokenOperationType.PASSWORD_RENEW)) {
            throw NotFoundException(I18Codes.I18TokenCodes.INVALID_OPERATION_TYPE)
        }

        if (token.isExpired()) {
            throw NotFoundException(I18Codes.I18TokenCodes.EXPIRED)
        }
    }
}
