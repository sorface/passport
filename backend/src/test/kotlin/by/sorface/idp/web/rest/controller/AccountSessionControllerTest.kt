package by.sorface.idp.web.rest.controller

import by.sorface.idp.web.rest.facade.AccountSessionFacade
import by.sorface.idp.web.rest.model.sessions.AccountCleanupSessionRequest
import by.sorface.idp.web.rest.model.sessions.AccountContextSession
import by.sorface.idp.web.rest.model.sessions.AccountSession
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class AccountSessionControllerTest {

    @MockK
    private lateinit var accountSessionFacade: AccountSessionFacade

    @InjectMockKs
    private lateinit var accountSessionController: AccountSessionController

    @Test
    fun `'find by username' when return one session`() {
        val username = "username"
        val accountSession = AccountSession().apply {
            id = "session-id"
        }

        every { accountSessionFacade.getAllByUsername(eq(username)) } returns AccountContextSession(sessions = listOf(accountSession))

        val accountContextSession = accountSessionController.getAllByUsername(username)

        verify(exactly = 1) { accountSessionFacade.getAllByUsername(eq(username)) }

        Assertions.assertEquals(1, accountContextSession.sessions.size)
        Assertions.assertNotNull(accountContextSession.sessions.find { it.id == "session-id" })
    }


    @Test
    fun `'delete multiple by username' when return one session`() {
        val username = "username"
        val accountSession = AccountSession().apply {
            id = "session-id"
        }
        val accountCleanupSessionRequest = AccountCleanupSessionRequest(sessions = listOf("session-id"))
        val accountContextSession = AccountContextSession(sessions = listOf(accountSession))

        every { accountSessionFacade.deleteMultipleByUsername(eq(username), accountCleanupSessionRequest) } returns accountContextSession

        val accountContextSessionResult = accountSessionController.deleteMultipleByUsername(username, accountCleanupSessionRequest)

        verify(exactly = 1) { accountSessionFacade.deleteMultipleByUsername(eq(username), accountCleanupSessionRequest) }

        Assertions.assertEquals(1, accountContextSessionResult.sessions.size)
        Assertions.assertNotNull(accountContextSessionResult.sessions.find { it.id == "session-id" })
    }

    @Test
    fun `'get all' when return one session`() {
        val accountSession = AccountSession().apply {
            id = "session-id"
        }
        val accountContextSession = AccountContextSession(sessions = listOf(accountSession))

        every { accountSessionFacade.getAll() } returns accountContextSession

        val accountContextSessionResult = accountSessionController.getAll()

        verify(exactly = 1) { accountSessionFacade.getAll() }

        Assertions.assertEquals(1, accountContextSessionResult.sessions.size)
        Assertions.assertNotNull(accountContextSessionResult.sessions.find { it.id == "session-id" })
    }

    @Test
    fun `'delete multiple' when return one session`() {
        val accountSession = AccountSession().apply {
            id = "session-id"
        }
        val accountCleanupSessionRequest = AccountCleanupSessionRequest(sessions = listOf("session-id"))
        val accountContextSession = AccountContextSession(sessions = listOf(accountSession))

        every { accountSessionFacade.deleteMultiple(accountCleanupSessionRequest) } returns accountContextSession

        val accountContextSessionResult = accountSessionController.deleteMultiple(accountCleanupSessionRequest)

        verify(exactly = 1) { accountSessionFacade.deleteMultiple(accountCleanupSessionRequest) }

        Assertions.assertEquals(1, accountContextSessionResult.sessions.size)
        Assertions.assertNotNull(accountContextSessionResult.sessions.find { it.id == "session-id" })
    }

}