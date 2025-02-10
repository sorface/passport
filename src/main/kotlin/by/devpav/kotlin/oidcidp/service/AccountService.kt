package by.devpav.kotlin.oidcidp.service

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.exceptions.GraphqlUserException
import by.devpav.kotlin.oidcidp.graphql.AccountUsername
import by.devpav.kotlin.oidcidp.graphql.PatchUpdateAccount
import by.devpav.kotlin.oidcidp.records.I18Codes
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService(private val userRepository: UserRepository) {

    fun findById(id: UUID): UserModel? = userRepository.findByIdOrNull(id)

    fun findByUsername(username: String): UserModel? = userRepository.findFirstByUsername(username)

    fun isExistByUsername(username: String): Boolean = userRepository.existsByUsername(username)

    @Transactional
    fun updateUsername(id: UUID, username: String) : AccountUsername {
        val user = userRepository.findByIdOrNull(id)

        user ?: throw GraphqlUserException(I18Codes.I18UserCodes.NOT_FOUND_BY_EMAIL, mapOf("id" to id))

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
    fun updateInformation(account: PatchUpdateAccount): UserModel? {
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