package by.sorface.passport.web.extensions

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.records.responses.ProfileRecord


fun UserEntity.toProfile() = ProfileRecord(
    this.id,
    this.username,
    this.email,
    this.firstName,
    this.lastName,
    this.middleName,
    this.avatarUrl,
    this.roles.mapNotNull { it.value }
)