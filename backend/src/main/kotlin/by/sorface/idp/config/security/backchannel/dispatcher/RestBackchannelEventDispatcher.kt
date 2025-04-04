package by.sorface.idp.config.security.backchannel.dispatcher

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestOperations
import java.net.URI

/**
 * Класс определяет диспетчер событий обратного канала, который использует REST-операции для отправки событий.
 *
 * @param restOperations объект RestOperations, который используется для выполнения HTTP-запросов.
 */
class RestBackchannelEventDispatcher(private val restOperations: RestOperations) : BackchannelEventDispatcher {

    private val logger = LoggerFactory.getLogger(RestBackchannelEventDispatcher::class.java)

    private var backchannelLogoutPath = "/logout/connect/back-channel"

    companion object {
        private const val LOGOUT_TOKEN_PARAMETER_NAME = "logout_token"
    }

    override fun dispatch(event: BackchannelLogoutEvent) {
        val registeredClient = event.registeredClient

        val backchannelUrls = registeredClient.redirectUris
            .filterNotNull()
            .filter { url -> URI.create(url).path.startsWith(backchannelLogoutPath) }

        if (backchannelUrls.isEmpty()) {
            logger.debug("No backchannel logout urls found for client with id ${registeredClient.clientId}")

            return
        }

        if (backchannelUrls.size > 1) {
            logger.warn("Multiple backchannel logout urls not supported. Client with id ${registeredClient.clientId} has ${backchannelUrls.size} urls")

            return
        }

        val backchannelEndpoint = backchannelUrls.first()

        val webForm = LinkedMultiValueMap<String, String>().apply {
            add(LOGOUT_TOKEN_PARAMETER_NAME, event.jwt.tokenValue)
        }

        val headers: HttpHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED;
        }

        val request = HttpEntity(webForm, headers)

        val result = kotlin.runCatching {
            restOperations.postForEntity(backchannelEndpoint, request, String::class.java)
        }

        if (result.isSuccess) {
            logger.debug("backchannel event dispatched to $backchannelEndpoint success")

            return
        }

        val resultException = result.exceptionOrNull()

        if (resultException == null) {
            logger.error("backchannel event dispatched to $backchannelEndpoint failed")

            return
        }

        logger.error("backchannel event dispatched to $backchannelEndpoint failed. Message: ${resultException.message}")
    }

}