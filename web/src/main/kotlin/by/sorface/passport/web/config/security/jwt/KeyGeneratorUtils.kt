package by.sorface.passport.web.config.security.jwt

import java.security.KeyPair
import java.security.KeyPairGenerator

object KeyGeneratorUtils {

    fun generateRsaKey(keySize: Int): KeyPair {
        val keyPair: KeyPair

        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(keySize)

            keyPair = keyPairGenerator.generateKeyPair()
        } catch (ex: Exception) {
            throw IllegalStateException(ex)
        }

        return keyPair
    }

}
