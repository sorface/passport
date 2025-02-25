package by.devpav.kotlin.oidcidp.service.oauth.converters

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import org.springframework.core.convert.converter.Converter

interface PrincipalConverter : Converter<UserModel, SorfacePrincipal>
