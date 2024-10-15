package by.sorface.passport.web.services.users;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.ProviderType;
import by.sorface.passport.web.dao.sql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserEntity findById(final UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByUsername(final String username) {
        return userRepository.findFirstByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByEmail(final String email) {
        return userRepository.findFirstByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByUsernameOrEmail(final String username, final String email) {
        return userRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username, email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByProviderAndExternalId(ProviderType provider, String externalId) {
        return userRepository.findByProviderTypeAndExternalId(provider, externalId);
    }

    @Override
    @Transactional
    public UserEntity save(final UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistUsername(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

}
