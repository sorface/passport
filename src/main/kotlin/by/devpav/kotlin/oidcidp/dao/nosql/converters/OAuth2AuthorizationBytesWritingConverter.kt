package by.devpav.kotlin.oidcidp.dao.nosql.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

@Component
@WritingConverter
class OAuth2AuthorizationBytesWritingConverter : Converter<OAuth2Authorization, ByteArray> {

    override fun convert(source: OAuth2Authorization): ByteArray {
        try {
            ByteArrayOutputStream().use { bos ->
                ObjectOutputStream(bos).use { out ->
                    out.writeObject(source)
                    out.flush()
                    return bos.toByteArray()
                }
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

}
