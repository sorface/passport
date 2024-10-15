package by.sorface.passport.web.facade.session;

import by.sorface.passport.web.constants.SessionAttributes;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.records.sessions.CleanupSession;
import by.sorface.passport.web.records.sessions.UserContextSession;
import by.sorface.passport.web.records.sessions.UserSession;
import by.sorface.passport.web.services.sessions.AccountSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountSessionFacade implements AccountSessionFacade {

    private final AccountSessionService accountSessionService;

    private final UserAgentAnalyzer userAgentAnalyzer;

    @Override
    public UserContextSession findByUsername(final String username) {
        final var sessions = accountSessionService.findByUsername(username);

        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        final var userSessions = sessions.stream()
                .map(session -> this.buildUserSession(sessionId, session))
                .toList();

        return new UserContextSession(userSessions);
    }

    @Override
    public UserContextSession getCurrentActiveSessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        final List<Session> sessions = accountSessionService.findByUsername(principal.getUsername());

        final var userSessions = sessions.stream()
                .map(session -> this.buildUserSession(sessionId, session))
                .toList();

        return new UserContextSession(userSessions);
    }

    @Override
    public UserContextSession deleteSessions(final CleanupSession cleanupSession) {
        final HashSet<String> deletedSessions = new HashSet<>(cleanupSession.sessions());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        final List<Session> sessions = accountSessionService.findByUsername(principal.getUsername());

        final List<Session> sessionsForDelete = sessions.stream()
                .filter(it -> deletedSessions.contains(it.getId()))
                .toList();

        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        accountSessionService.batchDelete(sessionsForDelete.stream().map(Session::getId).collect(Collectors.toList()));

        final var userSessions = sessions.stream()
                .map(session -> this.buildUserSession(sessionId, session))
                .toList();

        return new UserContextSession(userSessions);
    }

    @Override
    public void batchDelete(final CleanupSession cleanupSession) {
        accountSessionService.batchDelete(cleanupSession.sessions());
    }

    private UserSession buildUserSession(final String activeId, final Session session) {
        final String attribute = session.getAttribute(SessionAttributes.USER_AGENT);

        UserAgent.ImmutableUserAgent immutableUserAgent = userAgentAnalyzer.parse(attribute);

        return UserSession.builder()
                .id(session.getId())
                .createdAt(session.getCreationTime().toEpochMilli())
                .browser(immutableUserAgent.getValue("AgentName"))
                .deviceBrand(immutableUserAgent.getValue("DeviceBrand"))
                .deviceType(immutableUserAgent.getValue("DeviceClass"))
                .device(immutableUserAgent.getValue("DeviceName"))
                .active(activeId.equalsIgnoreCase(session.getId()))
                .build();
    }
}
