package by.sorface.passport.web.dao.nosql.redis.models

open class OneTimePassword(val code: String) : BasicRedisEntity()