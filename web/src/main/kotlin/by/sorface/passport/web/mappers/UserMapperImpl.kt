package by.sorface.passport.web.mappers

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.records.requests.AccountSignup
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapperImpl(private val passwordEncoder: PasswordEncoder) : UserMapper {

    override fun map(accountSignup: AccountSignup): UserEntity {
        val userEntity = UserEntity()

        userEntity.username = accountSignup.username
        userEntity.email = accountSignup.email
        userEntity.enabled = false

        val passwordHash = passwordEncoder.encode(accountSignup.password)

        userEntity.password = passwordHash

        return userEntity
    }

}
