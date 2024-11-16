package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import by.sorface.passport.web.services.users.UserService
import by.sorface.passport.web.utils.NullUtils.setIfNonNull
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.Map
import kotlin.String

@Service
@RequiredArgsConstructor
open class DefaultAccountFacade(private val userService: UserService) : AccountFacade {

    @Transactional(readOnly = true)
    override fun getCurrent(id: UUID): ProfileRecord {
        val user = userService.findById(id) ?: throw NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, Map.of("id", id.toString()))

        return ProfileRecord(
            user.id,
            user.username,
            user.email,
            user.firstName,
            user.lastName,
            user.middleName,
            user.avatarUrl,
            user.roles.mapNotNull { it.value }
        )
    }


    override fun update(id: UUID, request: UserPatchUpdate) {
        val user = userService.findById(id) ?: throw NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, Map.of("id", id.toString()))

        request.firstname?.let { user.firstName = it }
        request.lastname?.let { user.lastName = it }

        userService.save(user)
    }
}
