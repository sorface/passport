package by.sorface.passport.web.facade.accounts;

import by.sorface.passport.web.records.requests.UserPatchUpdate;
import by.sorface.passport.web.records.responses.ProfileRecord;

import java.util.UUID;

public interface AccountFacade {

    ProfileRecord getCurrent(final UUID id);

    void update(final UUID id, UserPatchUpdate userPatchUpdate);

}
