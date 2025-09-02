package by.sorface.idp.config.security.jose

import by.sorface.idp.config.security.jose.KeyGeneratorUtils.generateRsaKey
import com.nimbusds.jose.jwk.RSAKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

object Jwks {

    fun generateRsa(): RSAKey {
        val keyPair = generateRsaKey()

        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID("fb93ddae-7203-474e-8b04-89d2afe2bedf")
            .build()
    }

}
