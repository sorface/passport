package by.sorface.passport.web.security.oauth2.client.provider.database

import by.sorface.passport.web.dao.sql.models.OAuth2Client
import java.util.*

interface OAuth2ClientService {

    fun save(oAuth2Client: OAuth2Client): OAuth2Client

    fun findByClientId(clientId: String): OAuth2Client?

    fun findById(id: UUID): OAuth2Client?

    fun findAllByUserId(id: UUID): List<OAuth2Client>

    fun findByIdAndUserId(id: UUID, userId: UUID): OAuth2Client?

    fun isExists(id: UUID): Boolean

    fun delete(id: UUID)

}