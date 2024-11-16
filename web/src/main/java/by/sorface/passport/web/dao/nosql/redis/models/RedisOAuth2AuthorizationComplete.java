package by.sorface.passport.web.dao.nosql.redis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("sorface.authorization:complete")
public class RedisOAuth2AuthorizationComplete {

    @Id
    private String id;

    @Indexed
    private String refreshToken;

    @Indexed
    private String oidcToken;

    @Indexed
    private String accessToken;

    private OAuth2Authorization authorization;

}
