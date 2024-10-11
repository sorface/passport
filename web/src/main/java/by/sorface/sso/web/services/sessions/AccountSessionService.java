package by.sorface.sso.web.services.sessions;

import org.springframework.session.Session;

import java.util.List;

public interface AccountSessionService {

    List<Session> findByUsername(final String username);

    void batchDelete(final List<String> sessionIds);

}
