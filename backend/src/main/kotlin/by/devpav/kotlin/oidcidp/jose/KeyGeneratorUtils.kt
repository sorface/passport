package by.devpav.kotlin.oidcidp.jose

import java.security.KeyPair
import java.security.KeyPairGenerator

object KeyGeneratorUtils {

    fun generateRsaKey(): KeyPair = KeyPairGenerator.getInstance("RSA").run {
        initialize(2048)
        generateKeyPair()
    }

}
