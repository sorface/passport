package by.sorface.passport.web.services.tokens;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;
import by.sorface.passport.web.dao.sql.RegistryTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTokenServiceTest {

    @Mock
    private RegistryTokenRepository registryTokenRepository;

    @InjectMocks
    private DefaultTokenService defaultTokenService;

    @Test
    void testFindByHash() {
        String hash = "testHash";
        TokenEntity tokenEntity = new TokenEntity();
        when(registryTokenRepository.findByHashIgnoreCase(hash)).thenReturn(tokenEntity);

        TokenEntity result = defaultTokenService.findByHash(hash);

        assertNotNull(result);
        assertEquals(tokenEntity, result);
        verify(registryTokenRepository, times(1)).findByHashIgnoreCase(hash);
    }

    @Test
    void testSaveForUser() {
        UserEntity userEntity = new UserEntity();
        TokenOperationType operationType = TokenOperationType.CONFIRM_EMAIL;

        when(registryTokenRepository.save(any(TokenEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TokenEntity result = defaultTokenService.saveForUser(userEntity, operationType);

        assertNotNull(result);
        assertEquals(userEntity, result.getUser());
        assertEquals(operationType, result.getOperationType());
        verify(registryTokenRepository, times(1)).save(any(TokenEntity.class));
    }

    @Test
    void testFindByEmail() {
        String email = "testEmail";
        TokenEntity tokenEntity = new TokenEntity();
        when(registryTokenRepository.findByUser_Email(email)).thenReturn(tokenEntity);

        TokenEntity result = defaultTokenService.findByEmail(email);

        assertNotNull(result);
        assertEquals(tokenEntity, result);
        verify(registryTokenRepository, times(1)).findByUser_Email(email);
    }

    @Test
    void testDeleteByHash() {
        String hash = "testHash";
        defaultTokenService.deleteByHash(hash);

        verify(registryTokenRepository, times(1)).deleteByHashIgnoreCase(hash);
    }
}
