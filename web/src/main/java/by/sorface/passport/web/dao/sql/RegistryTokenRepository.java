package by.sorface.passport.web.dao.sql;

import by.sorface.passport.web.dao.models.TokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistryTokenRepository extends BaseRepository<TokenEntity> {

    TokenEntity findByHashIgnoreCase(final String hash);

    TokenEntity findByUser_Email(final String email);

    void deleteByHashIgnoreCase(final String hash);

}
