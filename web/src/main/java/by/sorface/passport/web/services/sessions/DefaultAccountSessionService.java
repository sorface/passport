package by.sorface.passport.web.services.sessions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountSessionService implements AccountSessionService {

    private final RedisIndexedSessionRepository sessionRepository;

    @Override
    public List<Session> findByUsername(final String username) {
        final Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(username);

        return sessions.values().stream()
                .map(session -> (Session) session)
                .collect(Collectors.toList());
    }

    @Override
    public void batchDelete(final List<String> sessionIds) {
        sessionIds.forEach(sessionRepository::deleteById);
    }

}
