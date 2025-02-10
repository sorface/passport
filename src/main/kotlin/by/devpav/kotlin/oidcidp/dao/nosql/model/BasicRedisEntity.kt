package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.annotation.Id

open class BasicRedisEntity {

    @Id
    lateinit var id: String

}