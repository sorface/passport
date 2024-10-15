package by.sorface.passport.web.facade.signup;

import by.sorface.passport.web.exceptions.UserRequestException;
import by.sorface.passport.web.records.requests.AccountSignup;
import by.sorface.passport.web.records.requests.ConfirmEmail;
import by.sorface.passport.web.records.responses.UserConfirm;
import by.sorface.passport.web.records.responses.UserRegisteredHash;

/**
 *
 */
public interface SignupFacade {

    UserRegisteredHash signup(final AccountSignup user) throws UserRequestException;

    UserConfirm confirm(final ConfirmEmail token);

    UserRegisteredHash findTokenByEmail(final String email);

}
