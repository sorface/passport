package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockKExtension::class)
class AccountFacadeImplTest {

    @MockK
    private lateinit var authentication: Authentication

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var accountFacade: AccountFacadeImpl

    @BeforeEach
    fun initSecurityContext() {
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    fun `get status current authenticated user status when security context principal is null`() {
        every { authentication.principal } returns null
        every { authentication.isAuthenticated } returns false

        val accountAuthenticated = accountFacade.isAuthenticated()

        verify(exactly = 0) { authentication.principal }
        verify(exactly = 1) { authentication.isAuthenticated }

        Assertions.assertFalse(accountAuthenticated.access)
    }

}