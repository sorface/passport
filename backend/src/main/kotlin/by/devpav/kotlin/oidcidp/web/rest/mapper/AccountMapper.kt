package by.devpav.kotlin.oidcidp.web.rest.mapper

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.web.rest.model.accounts.Account
import by.devpav.kotlin.oidcidp.web.rest.model.sessions.AccountSession
import org.springframework.session.Session
import org.springframework.stereotype.Component

@Component
class UserConverter {

    fun convert(userModel: UserModel): Account {
        return Account(
            userModel.id,
            userModel.username,
            userModel.email,
            userModel.firstName,
            userModel.lastName,
            userModel.middleName,
            userModel.avatarUrl,
            userModel.roles.mapNotNull { it.value }
        )
    }

}

@Component
class UserSessionConverter {

    fun convert(activeId: String, session: Session): AccountSession {
        return AccountSession()
            .apply {
                id = session.id
                createdAt = session.creationTime.toEpochMilli()
                active = activeId.equals(session.id, ignoreCase = true)
            }
    }

}