package by.sorface.idp.service.oauth.converters

import by.sorface.idp.dao.sql.model.UserModel
import org.springframework.core.convert.converter.Converter
import ru.sorface.boot.security.core.principal.SorfacePrincipal

interface PrincipalConverter : Converter<UserModel, SorfacePrincipal>
