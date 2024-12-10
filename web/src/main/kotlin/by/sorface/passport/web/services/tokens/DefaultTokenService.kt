package by.sorface.passport.web.services.tokens

import by.sorface.passport.web.dao.sql.models.TokenEntity
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.TokenOperationType
import by.sorface.passport.web.dao.sql.repository.RegistryTokenRepository
import by.sorface.passport.web.utils.HashUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class DefaultTokenService(private val registryTokenRepository: RegistryTokenRepository) : TokenService {

    @Transactional(readOnly = true)
    override fun findByHash(hash: String?): TokenEntity? = registryTokenRepository.findByHashIgnoreCase(hash)

    @Transactional
    override fun saveForUser(user: UserEntity?, operationType: TokenOperationType?): TokenEntity {
        val hash = HashUtils.generateRegistryHash(50)

        val registryTokenEntity = TokenEntity()

        registryTokenEntity.hash = hash
        registryTokenEntity.user = user
        registryTokenEntity.operationType = operationType

        return registryTokenRepository.save(registryTokenEntity)
    }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String?): TokenEntity? = registryTokenRepository.findByUserEmail(email)

    @Transactional
    override fun deleteByHash(hash: String?) = registryTokenRepository.deleteByHashIgnoreCase(hash)
}
