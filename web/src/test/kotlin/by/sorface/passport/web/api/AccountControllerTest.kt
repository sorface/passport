package by.sorface.passport.web.api

import by.sorface.passport.web.facade.accounts.AccountFacade
import by.sorface.passport.web.records.requests.UserPatchUpdate
import by.sorface.passport.web.records.responses.ProfileRecord
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class AccountControllerTest {

    @Mock
    private lateinit var accountFacade: AccountFacade

    @InjectMocks
    private lateinit var accountController: AccountController

    companion object {
        private val profileRecord = ProfileRecord(
            id = UUID.randomUUID(),
            nickname = null,
            email = null,
            firstName = null,
            lastName = null,
            middleName = null,
            avatar = null,
            roles = listOf(),
        )
    }

    @Test
    fun `get current authorized user`() {
        Mockito.`when`(accountFacade.getCurrentAuthorizedUser()).thenReturn(profileRecord)

        accountController.getCurrentAuthorizedUser()

        Mockito.verify(accountFacade).getCurrentAuthorizedUser()
    }

    @Test
    fun `update user profile`() {
        val profileId = UUID.fromString("1e2eaf0d-a8d1-4204-b056-444116045e3f")
        val userPatchUpdate = UserPatchUpdate("DEFAULT_FIRSTNAME", "DEFAULT_LASTNAME");

        Mockito.`when`(accountFacade.update(profileId, userPatchUpdate)).thenReturn(profileRecord)

        accountController.update(userPatchUpdate, profileId)

        Mockito.verify(accountFacade).update(profileId, userPatchUpdate)
    }
}