package by.sorface.passport.web.security.oauth2.jwt

import com.nimbusds.jose.jwk.RSAKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

object Jwks {

    fun generateRsa(): RSAKey {
        val keyPair = KeyGeneratorUtils.generateRsaKey(2048)
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

}
