package by.sorface.passport.web.services.users.providers;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DefaultUserDatabaseProvider implements UserDetailsService {

    private final UserService userRepository;

    private final Converter<UserEntity, DefaultPrincipal> principalConverter;

    @Autowired
    public DefaultUserDatabaseProvider(@Qualifier("defaultUserService") final UserService userRepository,
                                       @Qualifier("defaultPrincipalConverter") final Converter<UserEntity, DefaultPrincipal> principalConverter) {
        this.userRepository = userRepository;
        this.principalConverter = principalConverter;
    }

    /**
     * Getting an authorized user from the database
     *
     * @param username the username identifying the user whose data is required.
     * @return user details
     * @throws UsernameNotFoundException when user not found by login or email
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var user = userRepository.findByUsernameOrEmail(username, username);

        return Optional.ofNullable(user)
                .map(principalConverter::convert)
                .orElseThrow(() -> new UsernameNotFoundException("User with username or email {%s} not found".formatted(username)));
    }

}
