package by.sorface.passport.web.dao.nosql.redis.models.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

@Component
@ReadingConverter
public class OAuth2AuthorizationBytesReadingConverter implements Converter<byte[], OAuth2Authorization> {

    @Override
    public OAuth2Authorization convert(byte[] source) {
        try (final ByteArrayInputStream bis = new ByteArrayInputStream(source); ObjectInput in = new ObjectInputStream(bis)) {
            return (OAuth2Authorization) in.readObject();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
