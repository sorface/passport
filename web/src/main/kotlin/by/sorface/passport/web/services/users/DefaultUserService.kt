package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import by.sorface.passport.web.dao.sql.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class DefaultUserService(private val userRepository: UserRepository) : UserService {

    @Transactional(readOnly = true)
    override fun findById(id: UUID): UserEntity? = userRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    override fun findByUsername(username: String): UserEntity? = userRepository.findFirstByUsernameIgnoreCase(username)

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): UserEntity? = userRepository.findFirstByEmailIgnoreCase(email)

    @Transactional(readOnly = true)
    override fun findByUsernameOrEmail(username: String, email: String): UserEntity? = userRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username, email)

    @Transactional(readOnly = true)
    override fun findByProviderAndExternalId(provider: ProviderType, externalId: String): UserEntity? =
        userRepository.findByProviderTypeAndExternalId(provider, externalId)

    @Transactional
    override fun save(user: UserEntity): UserEntity = userRepository.save(user)

    @Transactional(readOnly = true)
    override fun isExistUsername(username: String): Boolean = userRepository.existsByUsernameIgnoreCase(username)

}
