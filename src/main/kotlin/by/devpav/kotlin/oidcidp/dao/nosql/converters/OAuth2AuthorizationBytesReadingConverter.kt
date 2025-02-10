package by.devpav.kotlin.oidcidp.dao.nosql.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

@Component
@ReadingConverter
class OAuth2AuthorizationBytesReadingConverter : Converter<ByteArray, OAuth2Authorization> {

    override fun convert(source: ByteArray): OAuth2Authorization {
        try {
            ByteArrayInputStream(source).use { bis ->
                ObjectInputStream(bis).use { `in` ->
                    return `in`.readObject() as OAuth2Authorization
                }
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

}
