package by.sorface.passport.web.dao.sql;

import by.sorface.passport.web.dao.models.OAuth2Client;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OAuth2ClientRepository extends BaseRepository<OAuth2Client> {

    OAuth2Client findByClientId(final String id);

    OAuth2Client findByIdAndCreatedBy_Id(final UUID oauth2ClientId, final UUID userId);

    List<OAuth2Client> findAllByCreatedBy_Id(final UUID userId);

}
