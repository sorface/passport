package by.sorface.idp.config.security.backchannel.generator

import by.sorface.idp.config.security.backchannel.context.OidcContext
import by.sorface.idp.extencions.sessionId
import org.springframework.security.oauth2.client.oidc.authentication.logout.OidcLogoutToken
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.*
import java.time.Instant
import java.time.temporal.ChronoUnit

class JwtLogoutGenerator(private val jwtEncoder: JwtEncoder, private val jwtDecoder: JwtDecoder) : OidcLogoutTokenGenerator<Jwt> {

    override fun generate(context: OidcContext): Jwt {
        val oidcIdTokenJwt = jwtDecoder.decode(context.oidcIdToken())

        val issuer: String = oidcIdTokenJwt.issuer.toString()

        val registeredClient = context.registeredClient()

        val issuedAt = Instant.now()

        var jwsAlgorithm: JwsAlgorithm = SignatureAlgorithm.RS256

        val expiresAt: Instant = issuedAt.plus(30, ChronoUnit.MINUTES)

        if (registeredClient.tokenSettings.idTokenSignatureAlgorithm != null) {
            jwsAlgorithm = registeredClient.tokenSettings.idTokenSignatureAlgorithm
        }

        val jti = if (oidcIdTokenJwt.claims.containsKey("jti")) oidcIdTokenJwt.claims["jti"] as String else null

        val oidcLogoutToken = OidcLogoutToken.withTokenValue(oidcIdTokenJwt.tokenValue)
            .issuer(issuer)
            .subject(context.principalName())
            .audience(listOf(registeredClient.clientId))
            .issuedAt(issuedAt)
            .jti(jti)
            .sessionId(oidcIdTokenJwt.sessionId().toString())
            .claims { claims -> claims["exp"] = expiresAt }
            .build()

        val jwsHeader = JwsHeader.with(jwsAlgorithm).build()
        val claims = JwtClaimsSet.builder().claims { claims -> claims.putAll(oidcLogoutToken.claims) }.build()

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
    }

}