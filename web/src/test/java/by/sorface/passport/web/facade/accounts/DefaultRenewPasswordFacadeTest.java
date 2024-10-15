package by.sorface.passport.web.facade.accounts;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.exceptions.UserRequestException;
import by.sorface.passport.web.facade.emails.EmailLocaleMessageFacade;
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest;
import by.sorface.passport.web.services.tokens.TokenService;
import by.sorface.passport.web.services.tokens.TokenValidator;
import by.sorface.passport.web.services.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultRenewPasswordFacadeTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailLocaleMessageFacade emailLocaleMessageFacade;

    @InjectMocks
    private DefaultRenewPasswordFacade renewPasswordFacade;

    @Test
    void forgetPassword_UserFound_Success() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        when(userService.findByEmail(email)).thenReturn(user);
        when(tokenService.saveForUser(any(), any())).thenReturn(new TokenEntity());

        renewPasswordFacade.forgetPassword(email);

        verify(userService, times(1)).findByEmail(email);
        verify(tokenService, times(1)).saveForUser(any(), any());
        verify(emailLocaleMessageFacade, times(1)).sendRenewPasswordEmail(any(), any(), any(), any());
    }

    @Test
    void forgetPassword_UserNotFound_ThrowsException() {
        String email = "test@example.com";
        when(userService.findByEmail(email)).thenReturn(null);

        assertThrows(UserRequestException.class, () -> renewPasswordFacade.forgetPassword(email));

        verify(userService, times(1)).findByEmail(email);
        verifyNoInteractions(tokenService, emailLocaleMessageFacade);
    }

    @Test
    void applyNewPassword_Success() {
        ApplyNewPasswordRequest request = new ApplyNewPasswordRequest("newPassword", "tokenHash");
        TokenEntity token = Mockito.spy(new TokenEntity());
        UserEntity user = new UserEntity();
        when(tokenService.findByHash(anyString())).thenReturn(token);
        doNothing().when(tokenValidator).validateOperation(any(), any());
        doNothing().when(tokenValidator).validateExpiredDate(any());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(token.getUser()).thenReturn(user);
        when(token.getHash()).thenReturn("tokenHash");

        renewPasswordFacade.applyNewPassword(request);

        verify(tokenService, times(1)).findByHash(anyString());
        verify(tokenValidator, times(1)).validateOperation(any(), any());
        verify(tokenValidator, times(1)).validateExpiredDate(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userService, times(1)).save(any());
        verify(tokenService, times(1)).deleteByHash(anyString());
    }

    @Test
    void checkRenewPasswordToken_Success() {
        String hash = "tokenHash";
        TokenEntity token = new TokenEntity();
        when(tokenService.findByHash(anyString())).thenReturn(token);
        doNothing().when(tokenValidator).validateOperation(any(), any());
        doNothing().when(tokenValidator).validateExpiredDate(any());

        renewPasswordFacade.checkRenewPasswordToken(hash);

        verify(tokenService, times(1)).findByHash(anyString());
        verify(tokenValidator, times(1)).validateOperation(any(), any());
        verify(tokenValidator, times(1)).validateExpiredDate(any());
    }
}
