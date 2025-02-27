package by.devpav.kotlin.oidcidp.web.graphql.services.impl

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.graphql.model.AccountUsername
import by.devpav.kotlin.oidcidp.graphql.model.PatchUpdateAccount
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.web.graphql.services.AccountService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultAccountService(private val userRepository: UserRepository) : AccountService {

    override fun findById(id: UUID): UserModel? = userRepository.findByIdOrNull(id)

    override fun findByUsername(username: String): UserModel? = userRepository.findFirstByUsername(username)

    override fun isExistByUsername(username: String): Boolean = userRepository.existsByUsername(username)

    @Transactional
    override fun updateUsername(id: UUID, username: String): AccountUsername {
        val user = userRepository.findByIdOrNull(id)

        user ?: throw GraphqlUserException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, mapOf("id" to id))

        if (userRepository.existsByUsername(username)) {
            throw GraphqlUserException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN)
        }

        user.username = username

        return userRepository.save(user).run {
            AccountUsername.builder()
                .setUsername(this.username)
                .build()
        }
    }

    @Transactional
    override fun updateInformation(account: PatchUpdateAccount): UserModel? {
        val user = userRepository.findByIdOrNull(account.id)

        user ?: throw GraphqlUserException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", account.id)))

        return userRepository.save(
            user.apply {
                account.firstName?.let { firstName = it }
                account.lastName?.let { lastName = it }
                account.middleName?.let { middleName = it }
            }
        )
    }

}