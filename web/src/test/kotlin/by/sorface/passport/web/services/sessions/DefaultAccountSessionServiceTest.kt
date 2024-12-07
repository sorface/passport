package by.sorface.passport.web.services.sessions

import by.sorface.passport.web.security.oauth2.services.DefaultAccountSessionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session

private const val DEFAULT_USERNAME = "sorface"

private const val DEFAULT_USER_SESSION_ID = "default_session_id"

@ExtendWith(MockitoExtension::class)
class DefaultAccountSessionServiceTest {

    @Mock
    private lateinit var sessionRepository: FindByIndexNameSessionRepository<out Session>

    @InjectMocks
    private lateinit var accountSessionService: DefaultAccountSessionService

    @Test
    fun `find sessions by username when session map is not empty`() {
        val stubSession = Mockito.mock(Session::class.java)

        val dictionarySessions = mapOf(Pair(DEFAULT_USER_SESSION_ID, stubSession))

        Mockito.`when`(sessionRepository.findByPrincipalName(DEFAULT_USERNAME)).thenReturn(dictionarySessions)

        val sessions = accountSessionService.findByUsername(DEFAULT_USERNAME)

        assertFalse(sessions.isEmpty())

        assertEquals(stubSession, sessions.first())
    }

    @Test
    fun `find sessions by username when session map is empty`() {
        Mockito.`when`(sessionRepository.findByPrincipalName(DEFAULT_USERNAME)).thenReturn(mapOf())

        val sessions = accountSessionService.findByUsername(DEFAULT_USERNAME)

        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `find sessions by username when session map is null`() {
        Mockito.`when`(sessionRepository.findByPrincipalName(DEFAULT_USERNAME)).thenReturn(null)

        val sessions = accountSessionService.findByUsername(DEFAULT_USERNAME)

        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `find session map by username when session map is empty`() {
        Mockito.`when`(sessionRepository.findByPrincipalName(DEFAULT_USERNAME)).thenReturn(mapOf())

        val sessions = accountSessionService.findDictionaryByUsername(DEFAULT_USERNAME)

        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `find session map by username when session map is null`() {
        Mockito.`when`(sessionRepository.findByPrincipalName(DEFAULT_USERNAME)).thenReturn(null)

        val sessions = accountSessionService.findDictionaryByUsername(DEFAULT_USERNAME)

        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `find session map by username when session map is not empty`() {
        val stubSession = Mockito.mock(Session::class.java)

        val dictionarySessions = mapOf(Pair(DEFAULT_USER_SESSION_ID, stubSession))

        Mockito.`when`(sessionRepository.findByPrincipalName(DEFAULT_USERNAME)).thenReturn(dictionarySessions)

        val sessions = accountSessionService.findDictionaryByUsername(DEFAULT_USERNAME)

        assertFalse(sessions.isEmpty())

        val sessionPair = sessions.entries.firstOrNull()

        assertNotNull(sessionPair)

        assertEquals(DEFAULT_USER_SESSION_ID, sessionPair?.key)
        assertEquals(stubSession, sessionPair?.value)
    }

    @Test
    fun `batch delete sessions by list ids when list is empty`() {
        accountSessionService.batchDeleteById(listOf())

        Mockito.verify(sessionRepository, Mockito.never()).deleteById(Mockito.any())
    }

    @Test
    fun `batch delete sessions by list ids when list is not empty`() {
        Mockito.doNothing().`when`(sessionRepository).deleteById(Mockito.eq(DEFAULT_USER_SESSION_ID))

        accountSessionService.batchDeleteById(listOf(DEFAULT_USER_SESSION_ID))

        Mockito.verify(sessionRepository, Mockito.times(1)).deleteById(Mockito.any())
    }

    @Test
    fun `batch delete sessions by list ids when list is not empty and contains less 1 duplicate elements`() {
        Mockito.doNothing().`when`(sessionRepository).deleteById(Mockito.eq(DEFAULT_USER_SESSION_ID))

        accountSessionService.batchDeleteById(listOf(DEFAULT_USER_SESSION_ID, DEFAULT_USER_SESSION_ID))

        Mockito.verify(sessionRepository, Mockito.times(1)).deleteById(Mockito.any())
    }

    @Test
    fun `batch delete sessions by list ids when list is not empty and contains less 1 elements`() {
        val anotherSessionId = "another_session_id"

        Mockito.doNothing().`when`(sessionRepository).deleteById(Mockito.eq(DEFAULT_USER_SESSION_ID))
        Mockito.doNothing().`when`(sessionRepository).deleteById(Mockito.eq(anotherSessionId))

        accountSessionService.batchDeleteById(listOf(DEFAULT_USER_SESSION_ID, anotherSessionId))

        Mockito.verify(sessionRepository, Mockito.times(2)).deleteById(Mockito.any())
    }
}