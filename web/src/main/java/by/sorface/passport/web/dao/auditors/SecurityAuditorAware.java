package by.sorface.passport.web.dao.auditors;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import by.sorface.passport.web.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<UserEntity> {

    private final UserService userService;

    /**
     * The getCurrentAuditor function is used to retrieve the current user from the SecurityContext.
     * The function returns an Optional&lt;UserEntity&gt; object, which may or may not contain a UserEntity object.
     * If there is no authenticated user in the SecurityContext, then an empty Optional will be returned.
     *
     * @return user entity
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.empty();
        }

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        return Optional.ofNullable(userService.findById(principal.getId()));
    }

}
