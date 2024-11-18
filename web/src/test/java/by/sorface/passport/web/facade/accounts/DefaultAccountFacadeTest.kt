package by.sorface.passport.web.facade.accounts;

import by.sorface.passport.web.dao.models.RoleEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.exceptions.NotFoundException;
import by.sorface.passport.web.records.requests.UserPatchUpdate;
import by.sorface.passport.web.records.responses.ProfileRecord;
import by.sorface.passport.web.services.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultAccountFacadeTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private DefaultAccountFacade accountFacade;

    @Test
    void testGetCurrentUser() {
        UUID userId = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername("testUser");
        user.setEmail("test@email.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("Middle");
        user.setAvatarUrl("avatar.jpg");
        RoleEntity roleUser = new RoleEntity();
        roleUser.setValue("ROLE_USER");
        user.setRoles(Collections.singletonList(roleUser));

        when(userService.findById(userId)).thenReturn(user);

        ProfileRecord profileRecord = accountFacade.getCurrent(userId);

        assertNotNull(profileRecord);
        assertEquals(userId, profileRecord.id);
        assertEquals(user.getUsername(), profileRecord.nickname);
        assertEquals(user.getEmail(), profileRecord.email);
        assertEquals(user.getFirstName(), profileRecord.firstName);
        assertEquals(user.getLastName(), profileRecord.lastName);
        assertEquals(user.getMiddleName(), profileRecord.middleName);
        assertEquals(user.getAvatarUrl(), profileRecord.avatar);
        assertEquals(user.getRoles().get(0).getValue(), profileRecord.roles.get(0));

        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testGetCurrentUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> accountFacade.getCurrent(userId));

        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");

        UserPatchUpdate userPatchUpdate = new UserPatchUpdate("NewFirstName", "NewLastName");

        when(userService.findById(userId)).thenReturn(user);

        accountFacade.update(userId, userPatchUpdate);

        verify(userService, times(1)).findById(userId);
        verify(userService, times(1)).save(user);
        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("NewLastName", user.getLastName());
    }

    @Test
    void testUpdateUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> accountFacade.update(userId, new UserPatchUpdate("NewFirstName", "NewLastName")));

        verify(userService, times(1)).findById(userId);
        verify(userService, never()).save(any());
    }
}