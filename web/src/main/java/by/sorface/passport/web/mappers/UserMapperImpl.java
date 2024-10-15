package by.sorface.passport.web.mappers;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.requests.AccountSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity map(AccountSignup user) {
        final var userEntity = new UserEntity();

        userEntity.setUsername(user.username());
        userEntity.setEmail(user.email());
        userEntity.setFirstName(user.firstName());
        userEntity.setLastName(user.lastName());
        userEntity.setMiddleName(user.middleName());
        userEntity.setEnabled(false);

        final var passwordHash = passwordEncoder.encode(user.password());

        userEntity.setPassword(passwordHash);

        return userEntity;
    }

}
