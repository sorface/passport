package by.sorface.passport.web.extensions

import by.sorface.passport.web.dao.models.TokenEntity
import by.sorface.passport.web.dao.models.enums.TokenOperationType
import java.time.LocalDateTime

fun TokenEntity.hasOperation(operationType: TokenOperationType) = this.operationType != operationType

fun TokenEntity.hasNotOperation(operationType: TokenOperationType) = !this.hasOperation(operationType)

fun TokenEntity.isExpired(): Boolean = this.modifiedDate?.isBefore(LocalDateTime.now()) != false
