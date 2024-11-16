package by.sorface.passport.web.services.users.providers

import by.sorface.passport.web.dao.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.services.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultUserDatabaseProvider @Autowired constructor(
    @param:Qualifier("defaultUserService") private val userRepository: UserService,
    @param:Qualifier("defaultPrincipalConverter") private val principalConverter: Converter<UserEntity, DefaultPrincipal>
) : UserDetailsService {
    /**
     * Getting an authorized user from the database
     *
     * @param username the username identifying the user whose data is required.
     * @return user details
     * @throws UsernameNotFoundException when user not found by login or email
     */
    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameOrEmail(username, username)

        return Optional.ofNullable(user)
            .map { source: UserEntity -> principalConverter.convert(source) }
            .orElseThrow { UsernameNotFoundException("User with username or email {%s} not found".formatted(username)) }
    }
}
