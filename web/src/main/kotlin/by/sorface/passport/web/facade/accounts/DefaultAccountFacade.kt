package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.extensions.toProfile
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import by.sorface.passport.web.security.extensions.getPrincipalIdOrThrow
import by.sorface.passport.web.services.users.UserService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultAccountFacade(private val userService: UserService) : AccountFacade {

    override fun getCurrentAuthorizedUser(): ProfileRecord {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrThrow(AccessDeniedException(I18Codes.I18GlobalCodes.ACCESS_DENIED))

        return userService.findByIdOrThrow(principalId) { userId ->
            NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, hashMapOf(Pair("id", userId.toString())))
        }.toProfile()
    }

    override fun update(id: UUID, request: UserPatchUpdate): ProfileRecord {
        val user = userService.findByIdOrThrow(id) { userId ->
            NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", userId.toString())))
        }

        request.firstname?.let { user.firstName = it }
        request.lastname?.let { user.lastName = it }

        return userService.save(user).toProfile()
    }

}