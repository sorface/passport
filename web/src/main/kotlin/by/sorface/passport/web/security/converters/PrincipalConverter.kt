package by.sorface.passport.web.security.converters

import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.records.principals.DefaultPrincipal
import org.springframework.core.convert.converter.Converter

interface PrincipalConverter : Converter<UserEntity, DefaultPrincipal>
