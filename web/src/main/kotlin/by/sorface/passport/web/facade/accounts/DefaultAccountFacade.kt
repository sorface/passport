package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.extensions.toProfile
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.accounts.AccountAuthenticated
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.requests.UserUsernamePatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import by.sorface.passport.web.records.responses.UserExistsResponse
import by.sorface.passport.web.security.extensions.getPrincipal
import by.sorface.passport.web.security.extensions.getPrincipalIdOrThrow
import by.sorface.passport.web.services.users.UserService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultAccountFacade(private val userService: UserService) : AccountFacade {
    override fun isAuthenticated(): AccountAuthenticated {
        val principal = SecurityContextHolder.getContext().getPrincipal()

        return AccountAuthenticated(principal != null)
    }


    override fun getCurrentAuthorizedUser(): ProfileRecord {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(AccessDeniedException(I18Codes.I18GlobalCodes.ACCESS_DENIED))

        return userService.findByIdOrThrow(principalId) { userId -> NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, hashMapOf(Pair("id", userId.toString()))) }
            .toProfile()
    }

    override fun update(id: UUID, request: UserPatchUpdate): ProfileRecord {
        val user = userService.findByIdOrThrow(id) { userId ->
            NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", userId.toString())))
        }

        request.firstname?.let { user.firstName = it }
        request.lastname?.let { user.lastName = it }


        return userService.save(user).toProfile()
    }

    override fun isExistsByUsername(username: String): UserExistsResponse = UserExistsResponse(userService.isExistUsername(username))

    override fun updateUsername(id: UUID, request: UserUsernamePatchUpdate): ProfileRecord {
        val user = userService.findByIdOrThrow(id) { userId ->
            NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", userId.toString())))
        }

        request.username?.let {
            if (userService.isExistUsername(it)) throw UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN)

            user.username = it
        }

        return userService.save(user).toProfile()
    }

}
