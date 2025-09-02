package by.sorface.idp.config.security.jose

import com.nimbusds.jose.jwk.RSAKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwksServiceImpl(@Value("\${security.jwks.keystore}") val jwtKeyJson: String) : JwksService {

    override fun getRsaKey(): RSAKey = RSAKey.parse(jwtKeyJson)

}