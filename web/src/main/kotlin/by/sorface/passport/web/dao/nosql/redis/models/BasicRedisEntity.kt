package by.sorface.passport.web.dao.nosql.redis.models

import org.springframework.data.annotation.Id

open class BasicRedisEntity {

    @Id
    lateinit var id: String

}