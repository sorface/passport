package by.sorface.passport.web.security.converters

import by.sorface.passport.web.dao.sql.models.OAuth2Client
import by.sorface.passport.web.records.responses.ApplicationClient
import org.springframework.stereotype.Component

@Component
class OAuth2ClientConverter {

    fun convertWithoutSecret(source: OAuth2Client): ApplicationClient {
        return convert(source, null)
    }

    fun convert(source: OAuth2Client, clientSecret: String? = source.clientSecret): ApplicationClient {
        val applicationClient = ApplicationClient()
            .apply {
                id = source.id.toString()
                clientId = source.clientId.toString()
                clientName = source.clientName.toString()
                this.clientSecret = clientSecret
                redirectUrls = (source.redirectUris ?: "").split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().toSet()
                expiresAt = source.clientSecretExpiresAt
                issueTime = source.clientIdIssueAt
                postLogoutUrl = source.postLogoutRedirectUri
            }

        applicationClient.id = source.id.toString()
        applicationClient.clientId = source.clientId.toString()
        applicationClient.clientName = source.clientName.toString()
        applicationClient.clientSecret = clientSecret
        applicationClient.redirectUrls = (source.redirectUris ?: "").split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().toSet()
        applicationClient.expiresAt = source.clientSecretExpiresAt
        applicationClient.issueTime = source.clientIdIssueAt
        applicationClient.postLogoutUrl = source.postLogoutRedirectUri

        return applicationClient
    }

}
