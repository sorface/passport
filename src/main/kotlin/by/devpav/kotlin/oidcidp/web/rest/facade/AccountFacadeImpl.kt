package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.extencions.getPrincipal
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrThrow
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.web.rest.exceptions.I18RestException
import by.devpav.kotlin.oidcidp.web.rest.mapper.UserConverter
import by.devpav.kotlin.oidcidp.web.rest.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountFacadeImpl(private val userRepository: UserRepository) : AccountFacade {

    @Autowired
    private lateinit var userConverter: UserConverter

    override fun isAuthenticated(): AccountAuthenticated {
        val principal = SecurityContextHolder.getContext().getPrincipal()

        return AccountAuthenticated(principal != null)
    }

    @Transactional(readOnly = true)
    override fun getCurrentAuthorized(): ProfileRecord {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(
            I18RestException(
                message = "User isn't authenticated",
                i18Code = I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED,
                httpStatus = HttpStatus.UNAUTHORIZED
            )
        )

        val userModel = userRepository.findByIdOrNull(principalId)
            ?: throw I18RestException(
                message = "User not found with id $principalId",
                i18Code = I18Codes.I18UserCodes.NOT_FOUND_BY_ID,
                i18Args = hashMapOf(Pair("id", principalId)),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        return userConverter.convert(userModel)
    }

    @Transactional
    override fun update(id: UUID, request: AccountPatchUpdate): ProfileRecord {
        val user = userRepository.findByIdOrNull(id)
            ?: throw I18RestException(
                message = "User not found with id $id",
                i18Code = I18Codes.I18UserCodes.NOT_FOUND_BY_ID,
                i18Args = hashMapOf(Pair("id", id)),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        request.firstname?.let { user.firstName = it }
        request.lastname?.let { user.lastName = it }

        return userRepository.save(user).let { userConverter.convert(it) }
    }

    override fun isExistsByUsername(username: String): AccountExistsResponse = AccountExistsResponse(userRepository.existsByUsername(username))

    override fun updateUsername(id: UUID, request: AccountUsernamePatchUpdate): ProfileRecord {
        val user = userRepository.findByIdOrNull(id)
            ?: throw I18RestException(
                message = "User not found with id $id",
                i18Code = I18Codes.I18UserCodes.NOT_FOUND_BY_ID,
                i18Args = hashMapOf(Pair("id", id)),
                httpStatus = HttpStatus.BAD_REQUEST
            )

        request.username.let {
            if (userRepository.existsByUsername(it)) {
                throw I18RestException(
                    message = "User already exists with login $it",
                    i18Code = I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN,
                    i18Args = hashMapOf(Pair("id", id)),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }

            user.username = it
        }

        return userRepository.save(user)
            .let {
                userConverter.convert(it)
            }
    }
}