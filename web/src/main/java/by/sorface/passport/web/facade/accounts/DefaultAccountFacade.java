package by.sorface.passport.web.facade.accounts;

import by.sorface.passport.web.dao.models.RoleEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.exceptions.NotFoundException;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.requests.UserPatchUpdate;
import by.sorface.passport.web.records.responses.ProfileRecord;
import by.sorface.passport.web.services.users.UserService;
import by.sorface.passport.web.utils.NullUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountFacade implements AccountFacade {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public ProfileRecord getCurrent(final UUID id) {
        final UserEntity user = userService.findById(id);

        if (Objects.isNull(user)) {
            throw new NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, Map.of("id", id.toString()));
        }

        return new ProfileRecord(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getAvatarUrl(),
                user.getRoles().stream().map(RoleEntity::getValue).toList());
    }


    @Override
    public void update(final UUID id, final UserPatchUpdate userPatchUpdate) {
        final UserEntity user = userService.findById(id);

        log.info("Updating user information by user ID '{}'", id);

        if (Objects.isNull(user)) {
            throw new NotFoundException(I18Codes.I18UserCodes.NOT_FOUND_BY_ID, Map.of("id", id.toString()));
        }

        NullUtils.setIfNonNull(userPatchUpdate.firstname(), user::setFirstName);
        NullUtils.setIfNonNull(userPatchUpdate.lastname(), user::setLastName);

        userService.save(user);
    }

}
