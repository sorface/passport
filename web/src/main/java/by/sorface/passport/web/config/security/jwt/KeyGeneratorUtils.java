package by.sorface.passport.web.config.security.jwt;

import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@UtilityClass
public class KeyGeneratorUtils {

    public static KeyPair generateRsaKey(int keySize) {
        KeyPair keyPair;

        try {
            final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);

            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        return keyPair;
    }
}
