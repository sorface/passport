package by.sorface.passport.web.mappers

import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.records.requests.AccountSignup

interface UserMapper {
    fun map(accountSignup: AccountSignup): UserEntity
}
