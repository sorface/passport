package by.sorface.passport.web.facade.signup;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;
import by.sorface.passport.web.exceptions.NotFoundException;
import by.sorface.passport.web.exceptions.UserRequestException;
import by.sorface.passport.web.mappers.UserMapper;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.requests.AccountSignup;
import by.sorface.passport.web.records.requests.ConfirmEmail;
import by.sorface.passport.web.records.responses.UserConfirm;
import by.sorface.passport.web.records.responses.UserRegisteredHash;
import by.sorface.passport.web.services.tokens.DefaultTokenValidator;
import by.sorface.passport.web.services.tokens.TokenService;
import by.sorface.passport.web.services.users.UserService;
import by.sorface.passport.web.utils.json.mask.MaskerFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultSignupFacade implements SignupFacade {

    private final UserService userService;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    private final DefaultTokenValidator defaultTokenValidator;

    @Override
    @Transactional
    public UserRegisteredHash signup(final AccountSignup user) throws UserRequestException {
        log.info("User registration request received");

        final UserEntity foundUserByEmail = userService.findByEmail(user.email());

        if (Objects.nonNull(foundUserByEmail)) {
            log.warn("A user with [id {}] and [email {}] already exists", foundUserByEmail.getId(), foundUserByEmail.getEmail());

            throw new UserRequestException(I18Codes.I18UserCodes.ALREADY_EXISTS_WITH_THIS_EMAIL);
        }

        final UserEntity foundUserByLogin = userService.findByUsername(user.username());

        if (Objects.nonNull(foundUserByLogin)) {
            log.warn("A user with [id {}] and [login {}] already exists", foundUserByLogin.getId(), foundUserByLogin.getEmail());
            throw new UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_LOGIN);
        }

        log.debug("Preparing the user for saving to the database");

        final UserEntity userEntity = userMapper.map(user);

        final UserEntity savedUser = userService.save(userEntity);

        log.info("A new user has been registered with the ID {}", savedUser.getId());

        log.info("Preparing a hash token for user");

        final TokenEntity registryToken = tokenService.saveForUser(savedUser, TokenOperationType.CONFIRM_EMAIL);

        log.info("The hash token {} created for account", registryToken.getHash().substring(0, 5).concat("..."));

        return new UserRegisteredHash(
                savedUser.getId(),
                savedUser.getEmail(),
                registryToken.getHash(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        );
    }

    @Override
    @Transactional
    public UserConfirm confirm(final ConfirmEmail confirmEmail) {
        log.info("Request for account confirmation using a token {}", MaskerFields.TOKEN.mask(confirmEmail.token()));

        final var token = tokenService.findByHash(confirmEmail.token());

        defaultTokenValidator.validateOperation(token, TokenOperationType.CONFIRM_EMAIL);
        defaultTokenValidator.validateExpiredDate(token);

        final UserEntity user = token.getUser();

        user.setEnabled(true);

        final UserEntity savedUser = userService.save(user);

        log.info("Account with ID {} confirmed by token hash {}", token.getUser().getId(), MaskerFields.TOKEN.mask(token.getHash()));

        return new UserConfirm(savedUser.getId(), savedUser.getEmail(), savedUser.isEnabled());
    }

    @Override
    @Transactional
    public UserRegisteredHash findTokenByEmail(final String email) {
        final var registryToken = tokenService.findByEmail(email);

        if (Objects.isNull(registryToken)) {
            log.warn("User not found by email {}", email);

            throw new NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND);
        }

        defaultTokenValidator.validateOperation(registryToken, TokenOperationType.CONFIRM_EMAIL);
        defaultTokenValidator.validateExpiredDate(registryToken);

        final UserEntity user = registryToken.getUser();

        return new UserRegisteredHash(
                user.getId(),
                user.getEmail(),
                registryToken.getHash(),
                user.getFirstName(),
                user.getLastName()
        );
    }

}
