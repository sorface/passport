package by.sorface.passport.web.security.config.formlogin

import by.sorface.passport.web.config.options.EndpointProperties
import by.sorface.passport.web.security.constants.SessionAttributes
import by.sorface.passport.web.security.extensions.useJsonStream
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.io.IOException
import java.util.*

@Component
open class SessionRedirectSuccessHandler(endpointProperties: EndpointProperties) : AbstractAuthenticationTargetUrlRequestHandler(), AuthenticationSuccessHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SessionRedirectSuccessHandler::class.java)
    }

    init {
        targetUrlParameter = "targetUrl"
        defaultTargetUrl = endpointProperties.uriPageProfile
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val requestAttributes = RequestContextHolder.currentRequestAttributes()

        request.getHeader(HttpHeaders.USER_AGENT)?.let { userAgent ->
            LOGGER.debug("user-agent [value -> {}] for session [id -> {}]", request.requestedSessionId, userAgent)

            requestAttributes.setAttribute(SessionAttributes.USER_AGENT, userAgent, RequestAttributes.SCOPE_SESSION)
        }

        val savedRequest = requestAttributes.getAttribute(SessionAttributes.SAVED_REQUEST, RequestAttributes.SCOPE_SESSION) as SavedRequest?

        if (savedRequest == null) {
            LOGGER.info("saved request is NULL for session [id -> {}]", request.requestedSessionId)

            response.useJsonStream<Map<String, String>>(HttpStatus.OK, mapOf())

            return
        }

        LOGGER.info("found saved request [url -> {}]. session [id -> {}]", savedRequest.redirectUrl, request.requestedSessionId)

        val targetUrlParameter = targetUrlParameter

        LOGGER.info("target url parameter [{}], session [id -> {}]", targetUrlParameter, request.requestedSessionId)

        if (isAlwaysUseDefaultTargetUrl || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            response.useJsonStream<Map<String, String>>(HttpStatus.OK, mapOf())
            return
        }

        LOGGER.info("clean session authentication for session [id -> {}]", request.requestedSessionId)

        clearAuthenticationAttributes(requestAttributes)

        val targetUrl = savedRequest.redirectUrl

        LOGGER.info("oauth2 redirect to target url -> {}. session [id -> {}]", targetUrl, request.requestedSessionId)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    private fun clearAuthenticationAttributes(requestAttributes: RequestAttributes) {
        val attribute = requestAttributes.getAttribute(SessionAttributes.AUTHENTICATION_EXCEPTION, RequestAttributes.SCOPE_SESSION)

        if (Objects.nonNull(attribute)) {
            requestAttributes.removeAttribute(SessionAttributes.AUTHENTICATION_EXCEPTION, RequestAttributes.SCOPE_SESSION)
        }
    }

    override fun isAlwaysUseDefaultTargetUrl(): Boolean {
        return false
    }
}
