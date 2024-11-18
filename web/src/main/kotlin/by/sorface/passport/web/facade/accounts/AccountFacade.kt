package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import java.util.*

interface AccountFacade {

    fun getCurrentAuthorizedUser(): ProfileRecord

    fun update(id: UUID, request: UserPatchUpdate)

}
