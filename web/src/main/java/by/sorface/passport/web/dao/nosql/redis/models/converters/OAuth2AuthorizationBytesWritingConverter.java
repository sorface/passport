package by.sorface.passport.web.dao.nosql.redis.models.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Component
@WritingConverter
public class OAuth2AuthorizationBytesWritingConverter implements Converter<OAuth2Authorization, byte[]> {

    private final Jackson2JsonRedisSerializer<OAuth2Authorization> serializer;

    public OAuth2AuthorizationBytesWritingConverter() {
        serializer = new Jackson2JsonRedisSerializer<>(new ObjectMapper(), OAuth2Authorization.class);
    }

    @Override
    public byte[] convert(OAuth2Authorization source) {
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(source);
            out.flush();
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
