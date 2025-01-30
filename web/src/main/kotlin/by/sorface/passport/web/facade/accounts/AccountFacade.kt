package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.records.accounts.AccountAuthenticated
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.requests.UserUsernamePatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import by.sorface.passport.web.records.responses.UserExistsResponse
import java.util.*

interface AccountFacade {

    fun isAuthenticated(): AccountAuthenticated

    fun getCurrentAuthorizedUser(): ProfileRecord

    fun update(id: UUID, request: UserPatchUpdate): ProfileRecord

    fun isExistsByUsername(username: String): UserExistsResponse

    fun updateUsername(id: UUID, request: UserUsernamePatchUpdate): ProfileRecord

}
