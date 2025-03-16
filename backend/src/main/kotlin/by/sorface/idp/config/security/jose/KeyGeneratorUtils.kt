package by.sorface.idp.config.security.jose

import java.security.KeyPair
import java.security.KeyPairGenerator

object KeyGeneratorUtils {

    fun generateRsaKey(): KeyPair = KeyPairGenerator.getInstance("RSA").run {
        initialize(2048)
        generateKeyPair()
    }

}
