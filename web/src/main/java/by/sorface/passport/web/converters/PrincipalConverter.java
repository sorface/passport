package by.sorface.passport.web.converters;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import org.springframework.core.convert.converter.Converter;

public interface PrincipalConverter extends Converter<UserEntity, DefaultPrincipal> {
}
