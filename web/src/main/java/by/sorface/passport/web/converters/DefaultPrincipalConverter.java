package by.sorface.passport.web.converters;

import by.sorface.passport.web.dao.models.RoleEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.records.principals.DefaultPrincipal;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DefaultPrincipalConverter implements PrincipalConverter {

    /**
     * The convert function is used to convert a UserEntity object into a DefaultPrincipal object.
     * The function takes in the user entity and converts it into an array of GrantedAuthority objects,
     * which are then passed to the constructor for DefaultPrincipal.  The password is also converted from
     * an optional string (which may be null) to an empty string if it's null, or just returns the password if not.
     *
     * @param user Convert the userentity to a defaultprincipal
     * @return A defaultprincipal object
     */
    @Override
    public DefaultPrincipal convert(final UserEntity user) {
        final List<GrantedAuthority> authorities = this.convertRoles(user.getRoles());

        final String userPassword = Optional.ofNullable(user.getPassword()).orElse(Strings.EMPTY);

        final var sorfaceUser = new DefaultPrincipal(user.getUsername(), userPassword, true, authorities);
        {
            sorfaceUser.setId(user.getId());
            sorfaceUser.setFirstName(user.getFirstName());
            sorfaceUser.setLastName(user.getLastName());
            sorfaceUser.setMiddleName(user.getMiddleName());
            sorfaceUser.setAvatarUrl(user.getAvatarUrl());
            sorfaceUser.setEmail(user.getEmail());
            sorfaceUser.setBirthday(user.getBirthday());
            sorfaceUser.setConfirm(user.isConfirm());
        }

        return sorfaceUser;
    }

    /**
     * The convertRoles function is a helper function that converts the roles of a user into
     * GrantedAuthority objects. The GrantedAuthority object is used by Spring Security to determine
     * what permissions are granted to the user. In this case, we're simply using the role name as
     * the permission (i.e., &quot;ROLE_USER&quot; or &quot;ROLE_ADMIN&quot;). You could also use more complex logic if you wanted to.
     *
     * @param roles Convert the roles to a list of grantedauthority
     * @return A list of grantedauthority objects
     */
    private List<GrantedAuthority> convertRoles(final Collection<? extends RoleEntity> roles) {
        return Optional.ofNullable(roles).stream()
                .flatMap(Collection::stream)
                .map(role -> new SimpleGrantedAuthority(role.getValue()))
                .collect(Collectors.toList());
    }

}
