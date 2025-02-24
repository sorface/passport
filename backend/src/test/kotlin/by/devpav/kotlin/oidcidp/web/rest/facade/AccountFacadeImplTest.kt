package by.devpav.kotlin.oidcidp.web.rest.facade

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.records.I18Codes
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import by.devpav.kotlin.oidcidp.web.rest.exceptions.I18RestException
import by.devpav.kotlin.oidcidp.web.rest.mapper.UserConverter
import by.devpav.kotlin.oidcidp.web.rest.model.AccountPatchUpdate
import by.devpav.kotlin.oidcidp.web.rest.model.AccountUsernameUpdate
import by.devpav.kotlin.oidcidp.web.rest.model.ProfileRecord
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@ExtendWith(MockKExtension::class)
class AccountFacadeImplTest {

    @MockK
    private lateinit var authentication: Authentication

    @MockK
    private lateinit var userConverter: UserConverter

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var accountFacade: AccountFacadeImpl

    @BeforeEach
    fun initSecurityContext() {
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    fun `get status current authenticated user when security context principal is null`() {
        every { authentication.principal } returns null
        every { authentication.isAuthenticated } returns false

        val accountAuthenticated = accountFacade.isAuthenticated()

        verify(exactly = 0) { authentication.principal }
        verify(exactly = 1) { authentication.isAuthenticated }

        Assertions.assertFalse(accountAuthenticated.access)
    }

    @Test
    fun `get status current authenticated user when security context principal is not null`() {
        every { authentication.principal } returns SorfacePrincipal(UUID.randomUUID(), "test_user", "test_password")
        every { authentication.isAuthenticated } returns true

        val accountAuthenticated = accountFacade.isAuthenticated()

        verify(exactly = 1) { authentication.principal }
        verify(exactly = 1) { authentication.isAuthenticated }

        Assertions.assertTrue(accountAuthenticated.access)
    }

    @Test
    fun `get current authenticated user when security context principal is null`() {
        every { authentication.isAuthenticated } returns false

        val i18RestException = assertThrows<I18RestException> { accountFacade.getCurrentAuthorized() }

        verify(exactly = 1) { authentication.isAuthenticated }

        Assertions.assertEquals(I18Codes.I18AuthenticationCodes.NOT_AUTHENTICATED, i18RestException.i18Code)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, i18RestException.httpStatus)
        Assertions.assertNotNull(i18RestException.message)
    }

    @Test
    fun `get current authenticated user when security context principal is not null but not found in database by id`() {
        val stubPrincipalId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")
        val sorfacePrincipal = mockk<SorfacePrincipal>()

        every { authentication.isAuthenticated } returns true
        every { authentication.principal } returns sorfacePrincipal
        every { sorfacePrincipal.id } returns stubPrincipalId
        every { userRepository.findByIdOrNull(stubPrincipalId) } returns null

        val i18RestException = assertThrows<I18RestException> { accountFacade.getCurrentAuthorized() }

        verify(exactly = 1) { authentication.isAuthenticated }
        verify(exactly = 1) { authentication.principal }
        verify(exactly = 1) { sorfacePrincipal.id }
        verify(exactly = 1) { userRepository.findByIdOrNull(stubPrincipalId) }

        Assertions.assertEquals(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, i18RestException.i18Code)
        Assertions.assertTrue(i18RestException.i18Args.containsKey("id"))
        Assertions.assertTrue(i18RestException.i18Args.containsValue(stubPrincipalId))
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, i18RestException.httpStatus)
        Assertions.assertNotNull(i18RestException.message)
    }

    @Test
    fun `get current authenticated user when security context principal is not null and found in database by id`() {
        val stubPrincipalId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")
        val sorfacePrincipal = mockk<SorfacePrincipal>()
        val userModelMock = mockk<UserModel>()
        val profile = mockk<ProfileRecord>()

        every { authentication.isAuthenticated } returns true
        every { authentication.principal } returns sorfacePrincipal
        every { sorfacePrincipal.id } returns stubPrincipalId
        every { userRepository.findByIdOrNull(stubPrincipalId) } returns userModelMock
        every { userConverter.convert(userModelMock) } returns profile

        val currentAuthorizedUser = accountFacade.getCurrentAuthorized()

        verify(exactly = 1) { authentication.isAuthenticated }
        verify(exactly = 1) { authentication.principal }
        verify(exactly = 1) { sorfacePrincipal.id }
        verify(exactly = 1) { userRepository.findByIdOrNull(stubPrincipalId) }
        verify(exactly = 1) { userConverter.convert(userModelMock) }

        Assertions.assertEquals(profile, currentAuthorizedUser)
    }

    @Test
    fun `'update user' failure when user not found in database by id`() {
        val userId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")

        every { userRepository.findByIdOrNull(userId) } returns null

        val i18RestException = assertThrows<I18RestException> { accountFacade.update(userId, mockk()) }

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }

        Assertions.assertEquals(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, i18RestException.i18Code)

        Assertions.assertEquals(1, i18RestException.i18Args.size)
        Assertions.assertTrue(i18RestException.i18Args.containsKey("id"))
        Assertions.assertTrue(i18RestException.i18Args.containsValue(userId))

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, i18RestException.httpStatus)
        Assertions.assertNotNull(i18RestException.message)
    }

    @Test
    fun `'update user' success when user found in database by id and update only firstname`() {
        val userCaptor = slot<UserModel>()

        val userId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")
        val user = spyk<UserModel>()
        val updateAccountRequest = spyk<AccountPatchUpdate>()
        val profile = mockk<ProfileRecord>()

        every { userRepository.findByIdOrNull(userId) } returns user
        every { updateAccountRequest.firstname } returns "test"
        every { updateAccountRequest.lastname } returns null
        every { userRepository.save(user) } returns user
        every { userConverter.convert(user) } returns profile

        val profileResult = accountFacade.update(userId, updateAccountRequest)


        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
        verify(exactly = 1) { userRepository.save(capture(userCaptor)) }
        verify(exactly = 1) { userConverter.convert(user) }

        verify(exactly = 1) { user.firstName = eq("test") }
        verify(exactly = 0) { user.lastName = any() }

        verify(exactly = 1) { updateAccountRequest.firstname }
        verify(exactly = 1) { updateAccountRequest.lastname }

        Assertions.assertEquals(profile, profileResult)

        val userCaptureValue = userCaptor.captured

        Assertions.assertEquals("test", userCaptureValue.firstName)
    }

    @Test
    fun `'update user' success when user found in database by id and update only lastname`() {
        val userId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")
        val user = spyk<UserModel>()
        val updateAccountRequest = spyk<AccountPatchUpdate>()
        val profile = mockk<ProfileRecord>()

        every { userRepository.findByIdOrNull(userId) } returns user
        every { updateAccountRequest.firstname } returns null
        every { updateAccountRequest.lastname } returns "test"
        every { userRepository.save(user) } returns user
        every { userConverter.convert(user) } returns profile

        val profileResult = accountFacade.update(userId, updateAccountRequest)

        val userCaptor = slot<UserModel>()

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
        verify(exactly = 1) { userRepository.save(capture(userCaptor)) }
        verify(exactly = 1) { userConverter.convert(user) }

        verify(exactly = 1) { updateAccountRequest.firstname }
        verify(exactly = 1) { updateAccountRequest.lastname }

        verify(exactly = 0) { user.firstName = any() }
        verify(exactly = 1) { user.lastName = eq("test") }

        Assertions.assertEquals(profile, profileResult)

        val userCaptureValue = userCaptor.captured

        Assertions.assertEquals("test", userCaptureValue.lastName)
    }

    @Test
    fun `'is exists user by username' when user not exists by username`() {
        val username = "test_username"

        every { userRepository.existsByUsername(username) } returns false

        val accountExistsResponse = accountFacade.isExistsByUsername(username)

        Assertions.assertFalse(accountExistsResponse.exists)

        verify(exactly = 1) { userRepository.existsByUsername(username) }
    }

    @Test
    fun `'is exists user by username' when user exists by username`() {
        val username = "test_username"

        every { userRepository.existsByUsername(username) } returns true

        val accountExistsResponse = accountFacade.isExistsByUsername(username)

        Assertions.assertTrue(accountExistsResponse.exists)

        verify(exactly = 1) { userRepository.existsByUsername(username) }
    }

    @Test
    fun `'update user's username' when user not found by id`() {
        val userId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")

        every { userRepository.findByIdOrNull(userId) } returns null

        val i18RestException = assertThrows<I18RestException> { accountFacade.updateUsername(userId, mockk()) }

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }

        Assertions.assertEquals(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, i18RestException.i18Code)

        Assertions.assertEquals(1, i18RestException.i18Args.size)
        Assertions.assertTrue(i18RestException.i18Args.containsKey("id"))
        Assertions.assertTrue(i18RestException.i18Args.containsValue(userId))

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, i18RestException.httpStatus)
        Assertions.assertNotNull(i18RestException.message)
    }

    @Test
    fun `'update username' when user found by id but user already exists with the username`() {
        val request = mockk<AccountUsernameUpdate>()

        val requestedUsername = "test"

        val userId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")
        val userModel = mockk<UserModel>()

        every { userRepository.findByIdOrNull(userId) } returns userModel
        every { userRepository.existsByUsername(requestedUsername) } returns true

        every { request.username } returns requestedUsername

        val i18RestException = assertThrows<I18RestException> { accountFacade.updateUsername(userId, request) }

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
        verify(exactly = 1) { userRepository.existsByUsername(requestedUsername) }
        verify(exactly = 1) { request.username }

        Assertions.assertEquals(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_LOGIN, i18RestException.i18Code)
        Assertions.assertTrue(i18RestException.i18Args.isEmpty())
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, i18RestException.httpStatus)
        Assertions.assertNotNull(i18RestException.message)
    }

    @Test
    fun `'update username' when user found by id and user not exists with the username`() {
        val request = mockk<AccountUsernameUpdate>()

        val requestedUsername = "test"

        val userId = UUID.fromString("e74cf6d6-3b22-47b6-adfe-5a23c1a4c08c")
        val userModel = mockk<UserModel>()
        val profile = mockk<ProfileRecord>()

        every { userRepository.findByIdOrNull(userId) } returns userModel
        every { userRepository.existsByUsername(requestedUsername) } returns false
        every { userRepository.save(userModel) } returns userModel
        every { userConverter.convert(userModel) } returns profile

        every { userModel.username = any() } answers {}
        every { userModel.username } returns "current_username"

        every { request.username } returns requestedUsername

        val profileResult = accountFacade.updateUsername(userId, request)

        val usernameSlot = slot<String>()

        verify(exactly = 1) { userRepository.findByIdOrNull(userId) }
        verify(exactly = 1) { userRepository.existsByUsername(requestedUsername) }
        verify(exactly = 1) { userRepository.save(userModel) }
        verify(exactly = 1) { request.username }
        verify(exactly = 1) { userModel.username = capture(usernameSlot) }

        Assertions.assertEquals(profile, profileResult)

        Assertions.assertEquals(requestedUsername, usernameSlot.captured)
    }

}