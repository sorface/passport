package by.sorface.idp.config.security.oauth2.slo

import by.sorface.idp.config.web.properties.SessionCookieProperties
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.idp.dao.sql.repository.client.JpaRegisteredClientRepository
import by.sorface.idp.extencions.*
import by.sorface.idp.records.OperationError
import by.sorface.idp.utils.json.mask.MaskerFields
import by.sorface.passport.web.security.oauth2.slo.DelegateLogoutSuccessHandler
import io.micrometer.tracing.Tracer
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.session.data.redis.ReactiveRedisIndexedSessionRepository
import org.springframework.stereotype.Component

@Component
class SpecificLogoutAuthenticationFailureHandler(
    private val jwtDecoder: JwtDecoder,
    private val jpaRegisteredClientRepository: JpaRegisteredClientRepository,
    private val tracer: Tracer,
    private var sessionCookieProperties: SessionCookieProperties,
    private val redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository,
    private val sessionRepository: FindByIndexNameSessionRepository<out Session>
) : AuthenticationFailureHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val spanId = tracer.currentTraceContext().context()?.spanId()
        val traceId = tracer.currentTraceContext().context()?.traceId()

        val parameter = request.getParameter("post_logout_redirect_uri")
        val tokenHint = request.getParameter("id_token_hint")

        tokenHint ?: run {
            response.useErrorJsonStream(OperationError("id_token_hint not found", null, HttpStatus.BAD_REQUEST.value(), spanId, traceId))
            return
        }

        val jwt = jwtDecoder.decode(tokenHint)


        jwt ?: run {
            response.useErrorJsonStream(OperationError("jwt is not valid", null, HttpStatus.BAD_REQUEST.value(), spanId, traceId))
            return
        }

        val principalUsername = SecurityContextHolder.getContext().getPrincipalUsername()

        principalUsername ?: run {
            response.useErrorJsonStream(OperationError("username not found", null, HttpStatus.UNAUTHORIZED.value(), spanId, traceId))

            return
        }

        val selfLogout = jwt.subject == principalUsername

        if (selfLogout.not()) {
            response.useErrorJsonStream(OperationError("username has not access to logout", null, HttpStatus.FORBIDDEN.value(), spanId, traceId))

            return
        }

        var redirection: Boolean = false

        if (!parameter.isNullOrEmpty()) {
            val clientId = jwt.clientId()

            if (clientId.isNullOrEmpty()) {
                response.useErrorJsonStream(OperationError("client not found", null, HttpStatus.BAD_REQUEST.value(), spanId, traceId))

                return
            }

            val registeredClient = jpaRegisteredClientRepository.findByClientId(clientId)

            registeredClient ?: run {
                response.useErrorJsonStream(OperationError("Client not found", null, HttpStatus.BAD_REQUEST.value(), spanId, traceId))

                return
            }

            redirection = registeredClient.postLogoutRedirectUris.contains(parameter).not()
        }

        val authorizations = redisOAuth2AuthorizationCompleteRepository.findAllByPrincipalName(principalUsername)

        authorizations.forEach { redisOAuth2AuthorizationCompleteRepository.deleteById(it.id) }
        sessionRepository.findByPrincipalName(principalUsername).forEach { sessionRepository.deleteById(it.key) }

        CookieClearingLogoutHandler(sessionCookieProperties.name)
            .logout(request, response, SecurityContextHolder.getContext().authentication)

        if (redirection) {
            response.status = HttpStatus.FOUND.value()
            response.sendRedirect(parameter)
        }
    }

}