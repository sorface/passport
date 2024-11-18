package by.sorface.passport.web.facade.accounts

import by.sorface.passport.web.dao.sql.models.TokenEntity
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.exceptions.UserRequestException
import by.sorface.passport.web.facade.emails.EmailLocaleMessageFacade
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest
import by.sorface.passport.web.services.tokens.TokenService
import by.sorface.passport.web.services.users.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class DefaultRenewPasswordFacadeTest {
    @Mock
    private lateinit var tokenService: TokenService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var emailLocaleMessageFacade: EmailLocaleMessageFacade

    @InjectMocks
    private lateinit var renewPasswordFacade: DefaultRenewPasswordFacade

    @Test
    fun forgetPassword_UserFound_Success() {
        val email = "test@example.com"
        val user = UserEntity()
        user.email = email
        Mockito.`when`(userService.findByEmail(email)).thenReturn(user)
        Mockito.`when`(tokenService.saveForUser(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(TokenEntity())

        renewPasswordFacade.forgetPassword(email)

        Mockito.verify(userService, Mockito.times(1)).findByEmail(email)
        Mockito.verify(tokenService, Mockito.times(1)).saveForUser(ArgumentMatchers.any(), ArgumentMatchers.any())
        Mockito.verify(emailLocaleMessageFacade, Mockito.times(1))
            .sendRenewPasswordEmail(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun forgetPassword_UserNotFound_ThrowsException() {
        val email = "test@example.com"
        Mockito.`when`(userService.findByEmail(email)).thenReturn(null)

        Assertions.assertThrows(UserRequestException::class.java) { renewPasswordFacade.forgetPassword(email) }

        Mockito.verify(userService, Mockito.times(1)).findByEmail(email)
        Mockito.verifyNoInteractions(tokenService, emailLocaleMessageFacade)
    }

    @Test
    fun `apply new password success`() {
        val request = ApplyNewPasswordRequest("newPassword", "tokenHash")
        val token = Mockito.spy(TokenEntity())
        val user = UserEntity()

        Mockito.`when`(tokenService.findByHash(ArgumentMatchers.anyString())).thenReturn(token)
        Mockito.`when`(passwordEncoder.encode(ArgumentMatchers.any())).thenReturn("encodedPassword")
        Mockito.`when`(token.user).thenReturn(user)
        Mockito.`when`(token.hash).thenReturn("tokenHash")
        Mockito.`when`(token.modifiedDate).thenReturn(LocalDateTime.now())

        renewPasswordFacade.applyNewPassword(request)

        Mockito.verify(tokenService, Mockito.times(1)).findByHash(ArgumentMatchers.anyString())
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(ArgumentMatchers.any())
        Mockito.verify(userService, Mockito.times(1)).save(ArgumentMatchers.any())
        Mockito.verify(tokenService, Mockito.times(1)).deleteByHash(ArgumentMatchers.anyString())
    }

    @Test
    fun `check renew password token success`() {
        val hash = "tokenHash"
        val token = TokenEntity()
        Mockito.`when`(tokenService.findByHash(ArgumentMatchers.anyString())).thenReturn(token)

        renewPasswordFacade.checkRenewPasswordToken(hash)

        Mockito.verify(tokenService, Mockito.times(1)).findByHash(ArgumentMatchers.anyString())
    }
}
