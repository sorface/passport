package by.sorface.passport.web.services.users

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.ProviderType
import by.sorface.passport.web.dao.sql.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

private val DEFAULT_USER_ID = UUID.fromString("ecdd6db1-c058-4e2a-9b7c-861ddcdd69d9")
private const val DEFAULT_USER_NICKNAME = "TEST_USER_NAME"
private const val DEFAULT_USER_EMAIL = "TEST_USER_NAME@test.com"
private const val DEFAULT_USER_EXTERNAL_ID = "12345677890"

@ExtendWith(MockitoExtension::class)
class DefaultUserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var defaultUserService: DefaultUserService

    @Test
    fun `find user by id`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(stubUser) as Optional<UserEntity?>)

        val resultUser = defaultUserService.findById(DEFAULT_USER_ID)

        Mockito.verify(userRepository).findById(Mockito.eq(DEFAULT_USER_ID))

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `find user by id when user is not exists`() {
        Mockito.`when`(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.ofNullable(null) as Optional<UserEntity?>)

        val resultUser = defaultUserService.findById(DEFAULT_USER_ID)

        Mockito.verify(userRepository).findById(Mockito.eq(DEFAULT_USER_ID))

        assertNull(resultUser)
    }

    @Test
    fun `find user by id or throw when user is not found`() {
        Mockito.`when`(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.ofNullable(null) as Optional<UserEntity?>)

        val exceptionMessageBuilder = { id: UUID ->
            "User not found with id $id"
        }

        val runtimeException = assertThrows(RuntimeException::class.java) {
            defaultUserService.findByIdOrThrow(DEFAULT_USER_ID) { userId ->
                return@findByIdOrThrow RuntimeException(exceptionMessageBuilder.invoke(userId))
            }
        }

        Mockito.verify(userRepository).findById(Mockito.eq(DEFAULT_USER_ID))

        assertNotNull(runtimeException)
        assertNotNull(exceptionMessageBuilder.invoke(DEFAULT_USER_ID), runtimeException.message)
    }

    @Test
    fun `find user by id or throw when user found`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(stubUser) as Optional<UserEntity?>)

        val resultUser = defaultUserService.findByIdOrThrow(DEFAULT_USER_ID) { id: UUID -> RuntimeException("User not found with id $id") }

        Mockito.verify(userRepository).findById(Mockito.eq(DEFAULT_USER_ID))

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `find user by username when user is found`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.findFirstByUsernameIgnoreCase(DEFAULT_USER_NICKNAME)).thenReturn(stubUser)

        val resultUser = defaultUserService.findByUsername(DEFAULT_USER_NICKNAME)

        Mockito.verify(userRepository).findFirstByUsernameIgnoreCase(Mockito.eq(DEFAULT_USER_NICKNAME))

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `find user by username when user not found`() {
        Mockito.`when`(userRepository.findFirstByUsernameIgnoreCase(DEFAULT_USER_NICKNAME)).thenReturn(null)

        val resultUser = defaultUserService.findByUsername(DEFAULT_USER_NICKNAME)

        Mockito.verify(userRepository).findFirstByUsernameIgnoreCase(Mockito.eq(DEFAULT_USER_NICKNAME))

        assertNull(resultUser)
    }

    @Test
    fun `find user by email when user is found`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.findFirstByEmailIgnoreCase(DEFAULT_USER_EMAIL)).thenReturn(stubUser)

        val resultUser = defaultUserService.findByEmail(DEFAULT_USER_EMAIL)

        Mockito.verify(userRepository).findFirstByEmailIgnoreCase(Mockito.eq(DEFAULT_USER_EMAIL))

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `find user by email when user not found`() {
        Mockito.`when`(userRepository.findFirstByEmailIgnoreCase(DEFAULT_USER_EMAIL)).thenReturn(null)

        val resultUser = defaultUserService.findByEmail(DEFAULT_USER_EMAIL)

        Mockito.verify(userRepository).findFirstByEmailIgnoreCase(Mockito.eq(DEFAULT_USER_EMAIL))

        assertNull(resultUser)
    }

    @Test
    fun `find by username or email when user found by username or email`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(DEFAULT_USER_NICKNAME, DEFAULT_USER_EMAIL)).thenReturn(stubUser)

        val resultUser = defaultUserService.findByUsernameOrEmail(DEFAULT_USER_NICKNAME, DEFAULT_USER_EMAIL)

        Mockito.verify(userRepository).findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(DEFAULT_USER_NICKNAME, DEFAULT_USER_EMAIL)

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `find by username or email when user not found by username or email`() {
        Mockito.`when`(userRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(DEFAULT_USER_NICKNAME, DEFAULT_USER_EMAIL)).thenReturn(null)

        val resultUser = defaultUserService.findByUsernameOrEmail(DEFAULT_USER_NICKNAME, DEFAULT_USER_EMAIL)

        Mockito.verify(userRepository).findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(DEFAULT_USER_NICKNAME, DEFAULT_USER_EMAIL)

        assertNull(resultUser)
    }

    @Test
    fun `find user by provider and external id when user is found`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.findByProviderTypeAndExternalId(ProviderType.YANDEX, DEFAULT_USER_EXTERNAL_ID)).thenReturn(stubUser)

        val resultUser = defaultUserService.findByProviderAndExternalId(ProviderType.YANDEX, DEFAULT_USER_EXTERNAL_ID)

        Mockito.verify(userRepository).findByProviderTypeAndExternalId(ProviderType.YANDEX, DEFAULT_USER_EXTERNAL_ID)

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `find user by provider and external id when user is not found`() {
        Mockito.`when`(userRepository.findByProviderTypeAndExternalId(ProviderType.YANDEX, DEFAULT_USER_EXTERNAL_ID)).thenReturn(null)

        val resultUser = defaultUserService.findByProviderAndExternalId(ProviderType.YANDEX, DEFAULT_USER_EXTERNAL_ID)

        Mockito.verify(userRepository).findByProviderTypeAndExternalId(ProviderType.YANDEX, DEFAULT_USER_EXTERNAL_ID)

        assertNull(resultUser)
    }

    @Test
    fun `save or update user`() {
        val stubUser = UserEntity()

        Mockito.`when`(userRepository.save(stubUser)).thenReturn(stubUser)

        val resultUser = defaultUserService.save(stubUser)

        Mockito.verify(userRepository).save(stubUser)

        assertEquals(stubUser, resultUser)
    }

    @Test
    fun `user is exist by username`() {
        Mockito.`when`(userRepository.existsByUsernameIgnoreCase(DEFAULT_USER_NICKNAME)).thenReturn(true)

        val exist = defaultUserService.isExistUsername(DEFAULT_USER_NICKNAME)

        Mockito.verify(userRepository).existsByUsernameIgnoreCase(DEFAULT_USER_NICKNAME)

        assertTrue(exist)
    }

    @Test
    fun `user is not exist by username`() {
        Mockito.`when`(userRepository.existsByUsernameIgnoreCase(DEFAULT_USER_NICKNAME)).thenReturn(false)

        val exist = defaultUserService.isExistUsername(DEFAULT_USER_NICKNAME)

        Mockito.verify(userRepository).existsByUsernameIgnoreCase(DEFAULT_USER_NICKNAME)

        assertFalse(exist)
    }

    @Test
    fun `user is exist by email`() {
        Mockito.`when`(userRepository.existsByEmail(DEFAULT_USER_EMAIL)).thenReturn(true)

        val exist = defaultUserService.isExistEmail(DEFAULT_USER_EMAIL)

        Mockito.verify(userRepository).existsByEmail(DEFAULT_USER_EMAIL)

        assertTrue(exist)
    }

    @Test
    fun `user is not exist by email`() {
        Mockito.`when`(userRepository.existsByEmail(DEFAULT_USER_EMAIL)).thenReturn(false)

        val exist = defaultUserService.isExistEmail(DEFAULT_USER_EMAIL)

        Mockito.verify(userRepository).existsByEmail(DEFAULT_USER_EMAIL)

        assertFalse(exist)
    }
}
