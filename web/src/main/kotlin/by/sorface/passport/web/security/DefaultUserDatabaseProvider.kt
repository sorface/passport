package by.sorface.passport.web.security

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.extensions.toStringMask
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.services.users.UserService
import by.sorface.passport.web.utils.json.mask.MaskerFields
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultUserDatabaseProvider(
    @Qualifier("defaultUserService") private val userRepository: UserService,
    @Qualifier("defaultPrincipalConverter") private val principalConverter: Converter<UserEntity, DefaultPrincipal>
) : UserDetailsService {

    private val logger = LoggerFactory.getLogger(DefaultUserDatabaseProvider::class.java)

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails? =
        (userRepository.findByUsernameOrEmail(username, username) ?: throw UsernameNotFoundException("user with username or email $username} not found"))
            .let {
                logger.info("user with username/email [$username/${it.email.toStringMask(MaskerFields.EMAILS)}] and ID [${it.id}] was loaded")

                principalConverter.convert(it)
            }

}
