package by.sorface.passport.web.services.tokens

import by.sorface.passport.web.dao.sql.models.TokenEntity
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.dao.sql.models.enums.TokenOperationType
import by.sorface.passport.web.dao.sql.repository.RegistryTokenRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension

@DisplayName(value = "Service for work with token")
@ExtendWith(MockitoExtension::class)
internal class DefaultTokenServiceTest {

    @Mock
    private lateinit var registryTokenRepository: RegistryTokenRepository

    @InjectMocks
    private lateinit var defaultTokenService: DefaultTokenService

    @Test
    fun `find token by hash`() {
        val hash = "testHash"
        val tokenEntity = TokenEntity()
        Mockito.`when`(registryTokenRepository.findByHashIgnoreCase(hash)).thenReturn(tokenEntity)

        val result = defaultTokenService.findByHash(hash)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(tokenEntity, result)
        Mockito.verify(registryTokenRepository, Mockito.times(1)).findByHashIgnoreCase(hash)
    }

    @Test
    fun `save token for user`() {
        val userEntity = UserEntity()
        val operationType = TokenOperationType.CONFIRM_EMAIL

        Mockito.`when`(registryTokenRepository.save(ArgumentMatchers.any(TokenEntity::class.java)))
            .thenAnswer { invocation: InvocationOnMock -> invocation.getArgument(0) }

        val result = defaultTokenService.saveForUser(userEntity, operationType)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(userEntity, result.user)
        Assertions.assertEquals(operationType, result.operationType)
        Mockito.verify(registryTokenRepository, Mockito.times(1)).save(ArgumentMatchers.any(TokenEntity::class.java))
    }

    @Test
    fun `find token by email`() {
        val email = "testEmail"
        val tokenEntity = TokenEntity()
        Mockito.`when`(registryTokenRepository.findByUserEmail(email)).thenReturn(tokenEntity)

        val result = defaultTokenService.findByEmail(email)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(tokenEntity, result)
        Mockito.verify(registryTokenRepository, Mockito.times(1)).findByUserEmail(email)
    }

    @Test
    fun `delete token by hash`() {
        val hash = "testHash"
        defaultTokenService.deleteByHash(hash)

        Mockito.verify(registryTokenRepository, Mockito.times(1)).deleteByHashIgnoreCase(hash)
    }
}
