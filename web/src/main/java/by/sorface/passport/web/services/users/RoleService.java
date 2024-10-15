package by.sorface.passport.web.services.users;

import by.sorface.passport.web.dao.models.RoleEntity;

public interface RoleService {

    /**
     * Find role by value
     *
     * @param value role value
     * @return role
     */
    RoleEntity findByValue(final String value);

}
