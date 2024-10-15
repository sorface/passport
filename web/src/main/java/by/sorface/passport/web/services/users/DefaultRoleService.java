package by.sorface.passport.web.services.users;

import by.sorface.passport.web.dao.models.RoleEntity;
import by.sorface.passport.web.dao.sql.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRoleService implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity findByValue(String name) {
        return roleRepository.findByValueIgnoreCase(name).orElse(null);
    }

}
