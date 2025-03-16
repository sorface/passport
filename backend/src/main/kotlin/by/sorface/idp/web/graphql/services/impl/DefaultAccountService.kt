package by.sorface.idp.web.graphql.services.impl

import by.sorface.idp.dao.sql.model.UserModel
import by.sorface.idp.dao.sql.repository.user.UserRepository
import by.sorface.idp.exceptions.GraphqlUserException
import by.sorface.idp.graphql.model.GQAccountUsername
import by.sorface.idp.graphql.model.GQPatchUpdateAccount
import by.sorface.idp.records.I18Codes
import by.sorface.idp.web.graphql.services.AccountService
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
    override fun updateUsername(id: UUID, username: String): GQAccountUsername {
        val user = userRepository.findByIdOrNull(id)

        user ?: throw GraphqlUserException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, mapOf("id" to id))

        if (userRepository.existsByUsername(username)) {
            throw GraphqlUserException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN)
        }

        user.username = username

        return userRepository.save(user).run {
            GQAccountUsername.builder()
                .setUsername(this.username)
                .build()
        }
    }

    @Transactional
    override fun updateInformation(account: GQPatchUpdateAccount): UserModel? {
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