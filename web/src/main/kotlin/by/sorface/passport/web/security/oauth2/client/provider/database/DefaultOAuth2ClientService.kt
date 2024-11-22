package by.sorface.passport.web.security.oauth2.client.provider.database

import by.sorface.passport.web.dao.sql.models.OAuth2Client
import by.sorface.passport.web.dao.sql.repository.OAuth2ClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class DefaultOAuth2ClientService(private val oAuth2ClientRepository: OAuth2ClientRepository) : OAuth2ClientService {

    @Transactional
    override fun save(oAuth2Client: OAuth2Client): OAuth2Client = oAuth2ClientRepository.save(oAuth2Client)

    @Transactional(readOnly = true)
    override fun findByClientId(clientId: String): OAuth2Client? = oAuth2ClientRepository.findByClientId(clientId)

    @Transactional(readOnly = true)
    override fun findById(id: UUID): OAuth2Client? = oAuth2ClientRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    override fun findAllByUserId(id: UUID): List<OAuth2Client> = oAuth2ClientRepository.findAllByCreatedById(id)

    @Transactional(readOnly = true)
    override fun findByIdAndUserId(id: UUID, userId: UUID): OAuth2Client? = oAuth2ClientRepository.findByIdAndCreatedById(id, userId)

    @Transactional(readOnly = true)
    override fun isExists(id: UUID): Boolean = oAuth2ClientRepository.existsById(id)

    @Transactional
    override fun delete(id: UUID) = oAuth2ClientRepository.deleteById(id)

}
