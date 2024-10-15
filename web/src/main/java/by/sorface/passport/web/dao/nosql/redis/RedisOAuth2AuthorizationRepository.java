package by.sorface.passport.web.dao.nosql.redis;

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2Authorization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface RedisOAuth2AuthorizationRepository extends CrudRepository<RedisOAuth2Authorization, String>, QueryByExampleExecutor<RedisOAuth2Authorization> {
}
