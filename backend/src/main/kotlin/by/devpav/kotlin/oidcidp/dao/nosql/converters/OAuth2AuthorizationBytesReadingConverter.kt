package by.devpav.kotlin.oidcidp.dao.nosql.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

/**
 * Конвертер для чтения OAuth2 авторизации из байтового массива.
 *
 * @author devpav
 */
@Component
@ReadingConverter
class OAuth2AuthorizationBytesReadingConverter : Converter<ByteArray, OAuth2Authorization> {

    /**
     * Преобразует байтовый массив в объект OAuth2Authorization.
     *
     * @param source Байтовый массив, который нужно преобразовать.
     * @return Объект OAuth2Authorization.
     * @throws RuntimeException Если возникает исключение при чтении объекта.
     */
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
