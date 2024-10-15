package by.sorface.passport.web.mappers;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.requests.AccountSignup;

public interface UserMapper {

    UserEntity map(final AccountSignup accountSignup);

}
