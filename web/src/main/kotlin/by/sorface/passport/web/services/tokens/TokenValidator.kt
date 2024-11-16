package by.sorface.passport.web.services.tokens

import by.sorface.passport.web.dao.models.TokenEntity
import by.sorface.passport.web.dao.models.enums.TokenOperationType

interface TokenValidator {
    fun validateOperation(token: TokenEntity?, operationType: TokenOperationType)

    fun validateExpiredDate(token: TokenEntity)
}
