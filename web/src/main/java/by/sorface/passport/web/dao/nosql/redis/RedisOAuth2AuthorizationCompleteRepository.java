package by.sorface.passport.web.dao.nosql.redis;

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationComplete;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

public interface RedisOAuth2AuthorizationCompleteRepository extends CrudRepository<RedisOAuth2AuthorizationComplete, String>, QueryByExampleExecutor<RedisOAuth2AuthorizationComplete> {

    Optional<RedisOAuth2AuthorizationComplete> findFirstByAccessToken(final String accessToken);

    Optional<RedisOAuth2AuthorizationComplete> findFirstByRefreshToken(final String refreshToken);

    Optional<RedisOAuth2AuthorizationComplete> findFirstByOidcToken(final String oidcToken);

}
