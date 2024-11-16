package by.sorface.passport.web.services.sessions

import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.session.Session
import org.springframework.session.data.redis.RedisIndexedSessionRepository
import org.springframework.stereotype.Service
import java.util.function.Consumer
import java.util.stream.Collectors

@Slf4j
@Service
@RequiredArgsConstructor
class DefaultAccountSessionService(private val sessionRepository: RedisIndexedSessionRepository) : AccountSessionService {

    override fun findByUsername(username: String): List<Session> {
        val sessions: Map<String, Session> = sessionRepository.findByPrincipalName(username)

        if (sessions.values.isEmpty()) {
            return listOf()
        }

        return sessions.values.map { it }
    }

    override fun batchDelete(sessionIds: List<String?>) {
        sessionIds.forEach(Consumer { sessionId: String? -> sessionRepository.deleteById(sessionId) })
    }
}
