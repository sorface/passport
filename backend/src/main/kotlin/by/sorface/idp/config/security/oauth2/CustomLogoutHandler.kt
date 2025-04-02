package by.sorface.idp.config.security.oauth2

import by.sorface.idp.config.security.jose.Jwks
import by.sorface.idp.dao.nosql.repository.RedisOAuth2AuthorizationCompleteRepository
import by.sorface.idp.service.oauth.nosql.RedisOAuth2AuthorizationService
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.oidc.authentication.logout.OidcLogoutToken
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class CustomLogoutHandler : LogoutHandler {

    private val logger = org.slf4j.LoggerFactory.getLogger(CustomLogoutHandler::class.java)

    private val restTemplate = RestTemplate()

    @Autowired
    private lateinit var redisOAuth2AuthorizationService: RedisOAuth2AuthorizationService

    @Autowired
    private lateinit var jwtDecoder: JwtDecoder

    @Autowired
    private lateinit var redisOAuth2AuthorizationCompleteRepository: RedisOAuth2AuthorizationCompleteRepository

    @Autowired
    private lateinit var findByIndexNameSessionRepository: FindByIndexNameSessionRepository<out Session>

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        if (authentication == null) return

        redisOAuth2AuthorizationCompleteRepository.findAllByPrincipalName(authentication.name).forEach { it ->
            if (it.oidcToken == null) {
                redisOAuth2AuthorizationCompleteRepository.deleteById(it.id)

                return@forEach
            }

            val jwt = jwtDecoder.decode(it.oidcToken)

            val oidcLogoutToken = OidcLogoutToken.withTokenValue(it.oidcToken)
                .issuer(jwt.issuer.toString())
                .subject(jwt.subject)
                .audience(jwt.audience)
                .issuedAt(jwt.issuedAt)
                .jti(jwt.getClaimAsString("jti"))
                .sessionId(jwt.claims["sid"].toString())
                .claims { claims -> claims["exp"] = jwt.claims["exp"] }
                .build()

            val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap()
            requestBody.add("logout_token", oidcLogoutToken.tokenValue)

            val headers: org.springframework.http.HttpHeaders = org.springframework.http.HttpHeaders()
            headers.add(org.springframework.http.HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")

            val requestEntity: HttpEntity<*> = HttpEntity(requestBody, headers)

            val runCatching = runCatching {
                restTemplate.postForEntity("http://localhost:9000/logout/connect/back-channel/passport", requestEntity, String::class.java)
            }

            println("dropped session: ${runCatching.isFailure}")
        }

        val sessions = findByIndexNameSessionRepository.findByPrincipalName(authentication.name)

        sessions.forEach {
            findByIndexNameSessionRepository.deleteById(it.key)
        }
    }

}