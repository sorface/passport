package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.dao.models.enums.ProviderType
import java.util.*

interface UserService {

    fun findById(id: UUID): UserEntity?

    fun findByUsername(username: String): UserEntity?

    fun findByEmail(email: String): UserEntity?

    fun findByUsernameOrEmail(username: String, email: String): UserEntity?

    fun findByProviderAndExternalId(provider: ProviderType, externalId: String): UserEntity?

    fun save(user: UserEntity): UserEntity

    fun isExistUsername(username: String): Boolean

}
