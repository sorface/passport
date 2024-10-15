package by.sorface.passport.web.services.tokens;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;
import by.sorface.passport.web.dao.sql.RegistryTokenRepository;
import by.sorface.passport.web.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultTokenService implements TokenService {

    private final RegistryTokenRepository registryTokenRepository;

    @Override
    @Transactional(readOnly = true)
    public TokenEntity findByHash(final String hash) {
        return registryTokenRepository.findByHashIgnoreCase(hash);
    }

    @Transactional
    public TokenEntity saveForUser(final UserEntity user, final TokenOperationType operationType) {
        final String hash = HashUtils.generateRegistryHash(50);

        final var registryTokenEntity = new TokenEntity();
        {
            registryTokenEntity.setHash(hash);
            registryTokenEntity.setUser(user);
            registryTokenEntity.setOperationType(operationType);
        }

        return registryTokenRepository.save(registryTokenEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenEntity findByEmail(final String email) {
        return registryTokenRepository.findByUser_Email(email);
    }

    @Override
    @Transactional
    public void deleteByHash(final String hash) {
        registryTokenRepository.deleteByHashIgnoreCase(hash);
    }

}
