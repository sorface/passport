package by.devpav.kotlin.oidcidp.web.rest.mapper

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.web.rest.model.ProfileRecord
import org.springframework.stereotype.Component

@Component
class UserConverter {

    fun convert(userModel: UserModel) : ProfileRecord {
        return ProfileRecord(
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