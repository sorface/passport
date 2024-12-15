package by.sorface.passport.web.api

import by.sorface.passport.web.facade.session.AccountSessionFacade
import by.sorface.passport.web.records.sessions.CleanupSession
import by.sorface.passport.web.records.sessions.UserContextSession
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

private const val DEFAULT_USERNAME = "DEFAULT_USERNAME"

@ExtendWith(MockitoExtension::class)
class AccountSessionControllerTest {

    @Mock
    private lateinit var accountSessionFacade: AccountSessionFacade

    @InjectMocks
    private lateinit var accountSessionController: AccountSessionController

    @Test
    fun `find by username`() {
        val userContextSession = UserContextSession(listOf())

        Mockito.`when`(accountSessionFacade.findByUsername(DEFAULT_USERNAME)).thenReturn(userContextSession)

        val userSession = accountSessionController.findByUsername(DEFAULT_USERNAME)

        Mockito.verify(accountSessionFacade).findByUsername(DEFAULT_USERNAME)

        Assertions.assertEquals(userContextSession, userSession)
    }

    @Test
    fun `batch delete session`() {
        val cleanupSession = CleanupSession(listOf())
        val userContextSession = UserContextSession(listOf())

        Mockito.`when`(accountSessionFacade.deleteSessions(cleanupSession)).thenReturn(userContextSession)

        accountSessionController.deleteCurrent(cleanupSession)

        Mockito.verify(accountSessionFacade).deleteSessions(cleanupSession)
    }

    @Test
    fun `get active sessions`() {
        val userContextSession = UserContextSession(listOf())

        Mockito.`when`(accountSessionFacade.getActiveSessions()).thenReturn(userContextSession)

        val result = accountSessionController.getActiveSessions()

        Mockito.verify(accountSessionFacade).getActiveSessions()

        Assertions.assertEquals(userContextSession, result)
    }

    @Test
    fun `delete current session`() {
        val cleanupSession = CleanupSession(listOf())

        val expectedUserContextSession = UserContextSession(listOf())

        Mockito.`when`(accountSessionFacade.deleteSessions(cleanupSession)).thenReturn(expectedUserContextSession)

        val result = accountSessionController.deleteCurrent(cleanupSession)

        Mockito.verify(accountSessionFacade).deleteSessions(cleanupSession)

        Assertions.assertEquals(expectedUserContextSession, result)
    }

}