package by.sorface.passport.web.facade.accounts;

import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;
import by.sorface.passport.web.exceptions.UserRequestException;
import by.sorface.passport.web.facade.emails.EmailLocaleMessageFacade;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest;
import by.sorface.passport.web.services.tokens.TokenService;
import by.sorface.passport.web.services.tokens.TokenValidator;
import by.sorface.passport.web.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;

/**
 * DefaultRenewPasswordFacade class is a service class that implements RenewPasswordFacade interface.
 * It provides methods for forgetting password, applying new password and checking renewal password token.
 */
@Service
@RequiredArgsConstructor
public class DefaultRenewPasswordFacade implements RenewPasswordFacade {

    private final TokenService tokenService;

    private final TokenValidator tokenValidator;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EmailLocaleMessageFacade emailLocaleMessageFacade;

    /**
     * forgetPassword method is used to forget password.
     * It finds the user by email, generates a token for the user and sends an email with the token.
     * If the user is not found, it throws a UserRequestException.
     *
     * @param email the email of the user
     */
    @Override
    public void forgetPassword(final String email) {
        final var user = userService.findByEmail(email);

        if (Objects.isNull(user)) {
            throw new UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_EMAIL);
        }

        final var token = tokenService.saveForUser(user, TokenOperationType.PASSWORD_RENEW);

        final Locale locale = LocaleContextHolder.getLocale();

        emailLocaleMessageFacade.sendRenewPasswordEmail(locale, user.getEmail(), token.getHash(), user.getUsername());
    }


    /**
     * applyNewPassword method is used to apply a new password.
     * It finds the token by hash, validates the token, encodes the new password, sets the new password to the user and saves the user.
     * It also deletes the token after the password is changed.
     *
     * @param request the request containing the new password and the hash of the token
     */
    @Override
    @Transactional
    public void applyNewPassword(final ApplyNewPasswordRequest request) {
        final var token = tokenService.findByHash(request.hashToken());

        tokenValidator.validateOperation(token, TokenOperationType.PASSWORD_RENEW);
        tokenValidator.validateExpiredDate(token);

        final UserEntity user = token.getUser();

        final String encodedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(encodedPassword);

        userService.save(user);

        tokenService.deleteByHash(token.getHash());
    }

    /**
     * checkRenewPasswordToken method is used to check a renewal password token.
     * It finds the token by hash and validates the token.
     *
     * @param hash the hash of the token
     */
    @Override
    public void checkRenewPasswordToken(final String hash) {
        final var token = tokenService.findByHash(hash);

        tokenValidator.validateOperation(token, TokenOperationType.PASSWORD_RENEW);
        tokenValidator.validateExpiredDate(token);
    }

}
