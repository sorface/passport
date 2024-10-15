package by.sorface.passport.web.services.tokens;

import by.sorface.passport.web.dao.models.TokenEntity;
import by.sorface.passport.web.dao.models.enums.TokenOperationType;
import by.sorface.passport.web.exceptions.NotFoundException;
import by.sorface.passport.web.exceptions.ObjectExpiredException;
import by.sorface.passport.web.exceptions.UserRequestException;
import by.sorface.passport.web.records.I18Codes;
import by.sorface.passport.web.utils.json.mask.MaskerFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class DefaultTokenValidator implements TokenValidator {

    @Override
    public void validateOperation(final TokenEntity token, final TokenOperationType operationType) {
        if (Objects.isNull(token)) {
            log.warn("The token for [{}] the account was not found", operationType.name());

            throw new NotFoundException(I18Codes.I18TokenCodes.NOT_FOUND);
        }

        if (!operationType.equals(token.getOperationType())) {
            throw new UserRequestException(I18Codes.I18TokenCodes.INVALID_OPERATION_TYPE);
        }
    }

    @Override
    public void validateExpiredDate(TokenEntity token) {
        log.info("The token received {}", MaskerFields.TOKEN.mask(token.getHash()));

        final LocalDateTime expiredDate = token.getModifiedDate().plusDays(30);

        if (expiredDate.isBefore(LocalDateTime.now())) {
            log.warn("The token's lifetime has expired. Token expired on  {}. The token belongs to the user with the id {}",
                    expiredDate, token.getUser().getId());

            throw new ObjectExpiredException(I18Codes.I18TokenCodes.EXPIRED);
        }
    }

}
