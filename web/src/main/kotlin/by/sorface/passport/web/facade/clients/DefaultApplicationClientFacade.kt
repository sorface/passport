package by.sorface.passport.web.facade.clients

import by.sorface.passport.web.dao.sql.models.OAuth2Client
import by.sorface.passport.web.exceptions.NotFoundException
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.records.I18Codes
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.records.requests.ApplicationClientPatchRequest
import by.sorface.passport.web.records.requests.ApplicationRegistry
import by.sorface.passport.web.records.responses.ApplicationClient
import by.sorface.passport.web.records.responses.ApplicationClientRefreshSecret
import by.sorface.passport.web.security.converters.OAuth2ClientConverter
import by.sorface.passport.web.security.oauth2.services.OAuth2ClientService
import by.sorface.passport.web.utils.HashUtils.generateRegistryHash
import by.sorface.passport.web.utils.URLUtils.isValidRedirectUrl
import io.micrometer.tracing.annotation.NewSpan
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*

@Service
open class DefaultApplicationClientFacade(
    private val oAuth2ClientService: OAuth2ClientService,
    private val oAuth2ClientConverter: OAuth2ClientConverter,
    private val passwordEncoder: PasswordEncoder
) : ApplicationClientFacade {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultApplicationClientFacade::class.java)
    }

    override fun findByCurrentUser(): List<ApplicationClient> {
        LOGGER.info("Find application clients by current user")

        val authentication = SecurityContextHolder.getContext().authentication

        val principal = authentication.principal as DefaultPrincipal

        val userId = principal.id ?: throw NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID)

        LOGGER.debug("Find application clients by current user [id -> {}]", userId)

        val applicationClients = oAuth2ClientService.findAllByUserId(userId).map { oAuth2ClientConverter.convertWithoutSecret(it) }.toList()

        LOGGER.info("Find application clients [ids -> ${applicationClients.map { it.id }.toList()}] by current user [id -> ${principal.id}]")

        return applicationClients
    }

    override fun getByIdAndCurrentUser(clientId: UUID): ApplicationClient? {
        val authentication = SecurityContextHolder.getContext().authentication

        val principal = authentication.principal as DefaultPrincipal

        val userId = principal.id ?: throw NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID)

        val oAuth2Client = oAuth2ClientService.findByIdAndUserId(clientId, userId)
            ?: throw NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", clientId.toString())))

        return oAuth2ClientConverter.convertWithoutSecret(oAuth2Client)
    }

    override fun refreshSecret(clientId: UUID): ApplicationClientRefreshSecret? {
        val authentication = SecurityContextHolder.getContext().authentication

        val principal = authentication.principal as DefaultPrincipal

        val userId = principal.id ?: throw NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID)

        val oAuth2Client = oAuth2ClientService.findByIdAndUserId(clientId, userId)
            ?: throw NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", clientId.toString())))

        val clientSecret = generateClientSecret()

        oAuth2Client.clientSecret = passwordEncoder.encode(clientSecret)
        oAuth2Client.clientSecretExpiresAt = buildDefaultClientSecretExpire()

        oAuth2ClientService.save(oAuth2Client)

        return ApplicationClientRefreshSecret().run {
            this.clientSecret = clientSecret
            this.expiresAt = buildDefaultClientSecretExpire()

            this
        }
    }

    override fun delete(clientId: UUID) {
        if (!oAuth2ClientService.isExists(clientId)) {
            throw NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", clientId.toString())))
        }

        oAuth2ClientService.delete(clientId)
    }

    override fun registry(registryClient: ApplicationRegistry): ApplicationClient? {
        val clientSecret = generateClientSecret()

        val oAuth2Client = OAuth2Client()
            .apply {
                clientId = generateRegistryHash(55) + "@sorface.oauth.client"
                this.clientSecret = passwordEncoder.encode(clientSecret)
                clientName = registryClient.name
                clientIdIssueAt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
                clientSecretExpiresAt = buildDefaultClientSecretExpire()
                redirectUris = java.lang.String.join(";", registryClient.redirectionUrls)
            }

        val oAuth2ClientSaved = oAuth2ClientService.save(oAuth2Client)

        return oAuth2ClientConverter.convert(oAuth2ClientSaved, clientSecret)
    }

    @Transactional
    @NewSpan("partial-update")
    @org.springframework.cloud.sleuth.annotation.NewSpan("partial-update")
    override fun partialUpdate(clientId: UUID, request: ApplicationClientPatchRequest): ApplicationClient? {
        LOGGER.info("patch update application client [id -> ${clientId}]")

        val oAuth2Client = oAuth2ClientService.findById(clientId)
            ?: throw NotFoundException(I18Codes.I18ClientCodes.NOT_FOUND_BY_ID, mapOf(Pair("id", clientId.toString())))

        LOGGER.info("found application client with [id -> ${clientId}]")

        request.redirectionUrls?.let { redirectUrls: Set<String> ->
            for (url in redirectUrls) {
                if (isValidRedirectUrl(url)) {
                    continue
                }

                throw UserRequestException(I18Codes.I18ClientCodes.REDIRECT_URL_NOT_VALID, mapOf(Pair("url", url)))
            }

            val redirects = java.lang.String.join(";", redirectUrls)
            oAuth2Client.redirectUris = redirects
        }

        request.postLogoutUrl?.let { postLogoutRedirectUri: String ->
            if (isValidRedirectUrl(postLogoutRedirectUri).not()) {
                throw UserRequestException(I18Codes.I18ClientCodes.REDIRECT_URL_NOT_VALID, mapOf(Pair("url", postLogoutRedirectUri)))
            }

            oAuth2Client.postLogoutRedirectUri = postLogoutRedirectUri
        }

        request.name?.let { oAuth2Client.clientName = it }

        oAuth2ClientService.save(oAuth2Client)

        return oAuth2ClientConverter.convertWithoutSecret(oAuth2Client)
    }

    private fun buildDefaultClientSecretExpire(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.now().plus(300, ChronoUnit.DAYS), ZoneOffset.UTC)
    }

    private fun generateClientSecret(): String {
        return generateRegistryHash(55)
    }
}
