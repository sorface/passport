package by.sorface.passport.web.facade.clients;

import by.sorface.passport.web.records.requests.ApplicationClientPatchRequest;
import by.sorface.passport.web.records.requests.ApplicationRegistry;
import by.sorface.passport.web.records.responses.ApplicationClient;
import by.sorface.passport.web.records.responses.ApplicationClientRefreshSecret;

import java.util.List;
import java.util.UUID;

public interface ApplicationClientFacade {

    List<ApplicationClient> findByCurrentUser();

    ApplicationClient getByIdAndCurrentUser(final UUID clientId);

    ApplicationClient registry(final ApplicationRegistry registryClient);

    ApplicationClient partialUpdate(final UUID clientId, final ApplicationClientPatchRequest request);

    ApplicationClientRefreshSecret refreshSecret(final UUID clientId);

    void delete(final UUID clientId);

}
