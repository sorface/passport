package by.sorface.idp.dao.nosql.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

/**
 * Компонент, который преобразует объект OAuth2Authorization в массив байтов.
 */
@Component
@WritingConverter
class OAuth2AuthorizationBytesWritingConverter : Converter<OAuth2Authorization, ByteArray> {

    /**
     * Преобразует объект OAuth2Authorization в массив байтов.
     *
     * @param source объект OAuth2Authorization, который нужно преобразовать.
     * @return массив байтов, представляющий объект OAuth2Authorization.
     * @throws RuntimeException если возникает исключение при преобразовании.
     */
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
