package by.sorface.passport.web.dao.nosql.redis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("sorface.oauth2-complete")
public class RedisOAuth2Authorization {

    @Id
    private String id;

    private String principalId;

    private String accessValueToken;

    private String refreshValueToken;

    private String principalUsername;

}
