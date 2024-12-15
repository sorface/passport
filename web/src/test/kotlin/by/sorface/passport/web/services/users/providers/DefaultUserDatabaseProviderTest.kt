package by.sorface.passport.web.services.users.providers

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import by.sorface.passport.web.security.DefaultUserDatabaseProvider
import by.sorface.passport.web.services.users.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UsernameNotFoundException

private const val DEFAULT_USERNAME_OR_EMAIL = "DEFAULT_USERNAME_OR_EMAIL"

@ExtendWith(MockitoExtension::class)
class DefaultUserDatabaseProviderTest {

    @Mock
    private lateinit var userRepository: UserService

    @Mock
    private lateinit var principalConverter: Converter<UserEntity, DefaultPrincipal>

    @InjectMocks
    private lateinit var userDatabaseProvider: DefaultUserDatabaseProvider

    @Test
    fun `load user by username when user not found`() {
        Mockito.`when`(userRepository.findByUsernameOrEmail(DEFAULT_USERNAME_OR_EMAIL, DEFAULT_USERNAME_OR_EMAIL)).thenReturn(null)

        org.junit.jupiter.api.assertThrows<UsernameNotFoundException> { userDatabaseProvider.loadUserByUsername(DEFAULT_USERNAME_OR_EMAIL) }

        Mockito.verify(userRepository).findByUsernameOrEmail(DEFAULT_USERNAME_OR_EMAIL, DEFAULT_USERNAME_OR_EMAIL)
        Mockito.verify(principalConverter, Mockito.never()).convert(Mockito.any())
    }

    @Test
    fun `load user by username when user found`() {
        val foundUser = UserEntity()
        val expectedPrincipal = Mockito.mock(DefaultPrincipal::class.java)!!

        Mockito.`when`(userRepository.findByUsernameOrEmail(DEFAULT_USERNAME_OR_EMAIL, DEFAULT_USERNAME_OR_EMAIL)).thenReturn(foundUser)
        Mockito.`when`(principalConverter.convert(Mockito.eq(foundUser))).thenReturn(expectedPrincipal)

        val userDetails = userDatabaseProvider.loadUserByUsername(DEFAULT_USERNAME_OR_EMAIL)

        Mockito.verify(userRepository).findByUsernameOrEmail(DEFAULT_USERNAME_OR_EMAIL, DEFAULT_USERNAME_OR_EMAIL)
        Mockito.verify(principalConverter, Mockito.times(1)).convert(Mockito.eq(foundUser))

        assertEquals(expectedPrincipal, userDetails)
    }
}