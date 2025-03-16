package by.sorface.idp.service.oauth.converters

import by.sorface.idp.dao.sql.model.UserModel
import by.sorface.idp.records.SorfacePrincipal
import org.springframework.core.convert.converter.Converter

interface PrincipalConverter : Converter<UserModel, SorfacePrincipal>
