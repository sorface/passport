package by.sorface.passport.web.config.security.handlers

import by.sorface.passport.web.config.options.EndpointOptions
import by.sorface.passport.web.constants.SessionAttributes
import by.sorface.passport.web.utils.json.Json
import jakarta.annotation.PostConstruct
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
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

@Slf4j
@Component
@RequiredArgsConstructor
class SavedRequestRedisSuccessHandler(private val endpointOptions: EndpointOptions) : AbstractAuthenticationTargetUrlRequestHandler(), AuthenticationSuccessHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SavedRequestRedisSuccessHandler::class.java)
    }

    init {
        targetUrlParameter = "targetUrl"
        defaultTargetUrl = endpointOptions.uriPageProfile
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        LOGGER.info("request session id -> {}", request.requestedSessionId)
        LOGGER.info("authorized user -> {}{}", System.lineSeparator(), Json.lazyStringify(authentication.principal))

        val requestAttributes = RequestContextHolder.currentRequestAttributes()

        val savedRequest = requestAttributes.getAttribute(SessionAttributes.SAVED_REQUEST, RequestAttributes.SCOPE_SESSION) as SavedRequest

        val userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT))

        if (userAgent.isPresent) {
            val userAgentLine = userAgent.get()

            LOGGER.debug("user-agent [value -> {}] for session [id -> {}]", request.requestedSessionId, userAgentLine)

            requestAttributes.setAttribute(SessionAttributes.USER_AGENT, userAgentLine, RequestAttributes.SCOPE_SESSION)
        }

        if (Objects.isNull(savedRequest)) {
            LOGGER.info("saved request is NULL for session [id -> {}]", request.requestedSessionId)

            super.handle(request, response, authentication)
            return
        }

        LOGGER.info("found saved request [url -> {}]. session [id -> {}]", savedRequest.redirectUrl, request.requestedSessionId)
        val targetUrlParameter = targetUrlParameter
        LOGGER.info("target url parameter [{}], session [id -> {}]", targetUrlParameter, request.requestedSessionId)

        if (isAlwaysUseDefaultTargetUrl || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            super.handle(request, response, authentication)
            return
        }

        LOGGER.info("clean session authentication for session [id -> {}]", request.requestedSessionId)
        clearAuthenticationAttributes(requestAttributes)
        val targetUrl = savedRequest.redirectUrl
        LOGGER.info("oauth2 redirect to target url -> {}. session [id -> {}]", targetUrl, request.requestedSessionId)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    fun clearAuthenticationAttributes(requestAttributes: RequestAttributes) {
        val attribute = requestAttributes.getAttribute(SessionAttributes.AUTHENTICATION_EXCEPTION, RequestAttributes.SCOPE_SESSION)

        if (Objects.nonNull(attribute)) {
            requestAttributes.removeAttribute(SessionAttributes.AUTHENTICATION_EXCEPTION, RequestAttributes.SCOPE_SESSION)
        }
    }

    override fun isAlwaysUseDefaultTargetUrl(): Boolean {
        return false
    }
}
