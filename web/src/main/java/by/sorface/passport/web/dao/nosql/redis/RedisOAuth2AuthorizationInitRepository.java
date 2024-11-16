package by.sorface.passport.web.dao.nosql.redis;

import by.sorface.passport.web.dao.nosql.redis.models.RedisOAuth2AuthorizationInit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

public interface RedisOAuth2AuthorizationInitRepository  extends CrudRepository<RedisOAuth2AuthorizationInit, String>,
        QueryByExampleExecutor<RedisOAuth2AuthorizationInit> {

    Optional<RedisOAuth2AuthorizationInit> findFirstByCode(String code);

    Optional<RedisOAuth2AuthorizationInit> findFirstByState(String state);

}
