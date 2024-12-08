package by.sorface.passport.web.facade.signup

import by.sorface.passport.web.dao.sql.models.TokenEntity
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.TokenOperationType
import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.exceptions.ObjectExpiredException
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.extensions.hasNotOperation
import by.sorface.passport.web.extensions.isExpired
import by.sorface.passport.web.mappers.UserMapper
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.requests.AccountSignup
import by.sorface.passport.web.records.requests.ConfirmEmail
import by.sorface.passport.web.records.responses.UserConfirm
import by.sorface.passport.web.records.responses.UserRegisteredHash
import by.sorface.passport.web.services.tokens.TokenService
import by.sorface.passport.web.services.users.UserService
import by.sorface.passport.web.utils.json.mask.MaskerFields
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class DefaultSignupFacade(private val userService: UserService, private val tokenService: TokenService, private val userMapper: UserMapper) : SignupFacade {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultSignupEmailFacade::class.java)
    }

    @Transactional
    @Throws(UserRequestException::class)
    override fun signup(accountSignup: AccountSignup): UserRegisteredHash {
        LOGGER.info("User registration request received")

        var user = userService.findByEmail(accountSignup.email)

        if (user != null) {
            LOGGER.warn("A user with [id {}] and [email {}] already exists", user.id, user.email)

            throw UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_EMAIL)
        }

        user = userService.findByUsername(accountSignup.username)

        if (user != null) {
            LOGGER.warn("A user with [id {}] and [login {}] already exists", user.id, user.email)

            throw UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_LOGIN)
        }

        LOGGER.debug("Preparing the user for saving to the database")

        val userEntity = userMapper.map(accountSignup)

        val savedUser = userService.save(userEntity)

        LOGGER.info("A new user has been registered with the ID {}", savedUser.id)

        LOGGER.info("Preparing a hash token for user")

        val registryToken = tokenService.saveForUser(savedUser, TokenOperationType.CONFIRM_EMAIL)

        LOGGER.info("The hash token {} created for account", registryToken.hash?.substring(0, 5) + "...")

        return UserRegisteredHash(
            savedUser.id!!,
            savedUser.username!!,
            savedUser.email!!,
            registryToken.hash,
            savedUser.firstName,
            savedUser.lastName
        )
    }

    @Transactional
    override fun confirm(token: ConfirmEmail): UserConfirm {
        LOGGER.info("Request for account confirmation using a token ${MaskerFields.TOKEN.mask(token.token)}")

        val loadedToken: TokenEntity = tokenService.findByHash(token.token) ?: throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)

        if (loadedToken.hasNotOperation(TokenOperationType.CONFIRM_EMAIL)) {
            throw UserRequestException(I18Codes.I18TokenCodes.INVALID_OPERATION_TYPE)
        }

        if (loadedToken.isExpired()) {
            throw ObjectExpiredException(I18Codes.I18TokenCodes.EXPIRED)
        }

        val user: UserEntity = loadedToken.user ?: throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)

        user.enabled = true

        val savedUser = userService.save(user)

        LOGGER.info("Account with ID {} confirmed by token hash {}", user.id, MaskerFields.TOKEN.mask(loadedToken.hash))

        return UserConfirm(savedUser.id, savedUser.email, savedUser.enabled)
    }

    @Transactional
    override fun findTokenByEmail(email: String?): UserRegisteredHash {
        val loadedToken = tokenService.findByEmail(email)

        if (loadedToken == null) {
            LOGGER.warn("User not found by email $email")

            throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)
        }

        if (loadedToken.hasNotOperation(TokenOperationType.CONFIRM_EMAIL)) {
            throw UserRequestException(I18Codes.I18TokenCodes.INVALID_OPERATION_TYPE)
        }

        if (loadedToken.isExpired()) {
            throw ObjectExpiredException(I18Codes.I18TokenCodes.EXPIRED)
        }

        val user: UserEntity = loadedToken.user ?: throw NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND)

        return UserRegisteredHash(
            user.id!!,
            user.username!!,
            user.email!!,
            loadedToken.hash,
            user.firstName,
            user.lastName
        )
    }
}
