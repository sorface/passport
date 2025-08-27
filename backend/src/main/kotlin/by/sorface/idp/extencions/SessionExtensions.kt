package by.sorface.idp.extencions

import org.springframework.session.Session
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun String.toSha256Hash(): String {
    val byteArray = this.toByteArray(StandardCharsets.US_ASCII)
    return MessageDigest.getInstance("SHA-256")
        .digest(byteArray)
        .toString()
}

fun Session.hasIdHash(hash: String): Boolean = hash == this.id.toSha256Hash()