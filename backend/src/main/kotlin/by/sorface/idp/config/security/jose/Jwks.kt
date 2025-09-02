package by.sorface.idp.config.security.jose

import by.sorface.idp.config.security.jose.KeyGeneratorUtils.generateRsaKey
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.RSAKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

object Jwks {

    /**
     * Generates RSA key for JWT signing and verification.
     *
     * @return RSAKey for use in JWT
     * @throws RuntimeException if key creation fails
     */
    fun generateRsa(): RSAKey {
        val keyPair = generateRsaKey()

        val publicKey = keyPair.public as? RSAPublicKey
            ?: throw IllegalStateException("Generated public key is not RSAPublicKey")

        val privateKey = keyPair.private as? RSAPrivateKey
            ?: throw IllegalStateException("Generated private key is not RSAPrivateKey")

        val keyId = UUID.randomUUID().toString()

        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .algorithm(JWSAlgorithm.RS256)
            .keyID(keyId)
            .build()
    }

}
