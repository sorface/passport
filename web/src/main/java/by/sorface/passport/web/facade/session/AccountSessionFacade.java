package by.sorface.passport.web.facade.session;

import by.sorface.passport.web.records.sessions.CleanupSession;
import by.sorface.passport.web.records.sessions.UserContextSession;

public interface AccountSessionFacade {

    UserContextSession findByUsername(final String username);

    UserContextSession getCurrentActiveSessions();

    UserContextSession deleteSessions(final CleanupSession cleanupSession);

    void batchDelete(final CleanupSession cleanupSession);

}
