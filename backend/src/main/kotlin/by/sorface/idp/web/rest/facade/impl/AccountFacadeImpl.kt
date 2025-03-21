package by.sorface.idp.web.rest.facade.impl

import by.sorface.idp.dao.sql.repository.user.UserRepository
import by.sorface.idp.extencions.getPrincipal
import by.sorface.idp.extencions.getPrincipalIdOrThrow
import by.sorface.idp.extencions.isAuthenticated
import by.sorface.idp.records.I18Codes
import by.sorface.idp.web.rest.exceptions.I18RestException
import by.sorface.idp.web.rest.facade.AccountFacade
import by.sorface.idp.web.rest.mapper.UserConverter
import by.sorface.idp.web.rest.model.accounts.*
import io.micrometer.tracing.annotation.NewSpan
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountFacadeImpl(private val userRepository: UserRepository) : AccountFacade {

    private final val logger = LoggerFactory.getLogger(AccountFacade::class.java)

    @Autowired
    private lateinit var userConverter: UserConverter

    @NewSpan("account-is-authenticated")
    override fun isAuthenticated(): AccountAuthenticated {
        logger.info("get current authorized user from security context")

        val authenticated = SecurityContextHolder.getContext().isAuthenticated()

        logger.info("user authenticated status in system is $authenticated")

        return AccountAuthenticated(SecurityContextHolder.getContext().isAuthenticated())
    }

    @Transactional(readOnly = true)
    override fun getCurrentAuthorized(): Account {
        logger.info("get current authorized user from security context")

        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(
            I18RestException(
                message = "User isn't authenticated",
                i18Code = I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED,
                httpStatus = HttpStatus.UNAUTHORIZED
            )
        )

        logger.info("get current authorized user by user id: $principalId")

        val userModel = userRepository.findByIdOrNull(principalId)
            ?: throw I18RestException(
                message = "User not found with id $principalId",
                i18Code = I18Codes.I18UserCodes.NOT_FOUND_BY_ID,
                i18Args = hashMapOf(Pair("id", principalId)),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        logger.info("user found with by id $principalId")

        return userConverter.convert(userModel)
    }

    @Transactional
    override fun update(id: UUID, request: AccountPatchUpdate): Account {
        logger.info("get user by id: $id")

        val user = userRepository.findByIdOrNull(id)
            ?: throw I18RestException(
                message = "User not found with id $id",
                i18Code = I18Codes.I18UserCodes.NOT_FOUND_BY_ID,
                i18Args = hashMapOf(Pair("id", id)),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        request.firstname?.let {
            logger.debug("update user firstname with id {}. From firstname {} to {}", id, user.firstName, it)

            user.firstName = it
        }
        request.lastname?.let {
            logger.debug("update user lastname with id {}. From lastname {} to {}", id, user.lastName, it)

            user.lastName = it
        }

        logger.info("save user information with user id: $id")

        return userRepository.save(user).let {
            logger.debug("convert user information with id {}", id)

            userConverter.convert(it)
        }
    }

    override fun isExistsByUsername(username: String): AccountExistsResponse = AccountExistsResponse(userRepository.existsByUsername(username))

    override fun isExistsByUsernameOrEmail(login: String): AccountExistsResponse = AccountExistsResponse(userRepository.existsByUsernameOrEmail(login, login))

    @Transactional
    override fun updateUsername(id: UUID, request: AccountUsernameUpdate): Account {
        logger.info("get user [id -> $id] from database")

        val user = userRepository.findByIdOrNull(id)
            ?: throw I18RestException(
                message = "User not found with id $id",
                i18Code = I18Codes.I18UserCodes.NOT_FOUND_BY_ID,
                i18Args = hashMapOf(Pair("id", id)),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        logger.info("user [id -> $id] found in database")

        request.username.let {
            logger.info("check exists username [username -> $it, id -> $id] in database")

            if (userRepository.existsByUsername(it)) {
                throw I18RestException(
                    message = "User already exists with login $it",
                    i18Code = I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN,
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }

            logger.info("change username [from -> ${user.username}, to -> $it, user id -> $id] in database")

            user.username = it
        }

        logger.info("update user [id -> $id] information in database")

        return userRepository.save(user)
            .let {
                logger.info("convert user [id -> {}] information", id)

                userConverter.convert(it)
            }
    }
}