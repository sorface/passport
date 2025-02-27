package by.devpav.kotlin.oidcidp.web.rest.mapper

import by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel
import by.devpav.kotlin.oidcidp.web.rest.model.apps.ApplicationClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class ApplicationConverter {

    fun convert(registeredClient: RegisteredClientModel): ApplicationClient {
        return ApplicationClient().apply {
            this.id = registeredClient.id.toString()
            this.clientId = registeredClient.clientId
            this.clientName = registeredClient.clientName
            this.issueTime = LocalDateTime.ofInstant(registeredClient.clientIdIssueAt, ZoneOffset.UTC);
            this.expiresAt = LocalDateTime.ofInstant(registeredClient.clientSecretExpiresAt, ZoneOffset.UTC);
            this.redirectUrls = registeredClient.redirectUris.mapNotNull { redirectUrl ->
                redirectUrl.url
            }.toSet()
        }
    }

    fun convert(registeredClient: RegisteredClient, rawSecret: String): ApplicationClient {
        return ApplicationClient().apply {
            this.id = registeredClient.id.toString()
            this.clientId = registeredClient.clientId
            this.clientName = registeredClient.clientName
            this.clientSecret = rawSecret
            this.issueTime = LocalDateTime.ofInstant(registeredClient.clientIdIssuedAt, ZoneOffset.UTC);
            this.expiresAt = LocalDateTime.ofInstant(registeredClient.clientSecretExpiresAt, ZoneOffset.UTC);
            this.redirectUrls = registeredClient.redirectUris.toSet()
        }
    }

}