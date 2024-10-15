package by.sorface.passport.web.services.tokens;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;

public interface TokenValidator {

    void validateOperation(final TokenEntity token, final TokenOperationType operationType);

    void validateExpiredDate(final TokenEntity token);

}
