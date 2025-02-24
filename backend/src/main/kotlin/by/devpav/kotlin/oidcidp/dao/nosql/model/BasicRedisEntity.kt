package by.devpav.kotlin.oidcidp.dao.nosql.model

import org.springframework.data.annotation.Id

/**
 * Базовый класс для сущностей, которые будут храниться в Redis.
 */
open class BasicRedisEntity {

    /**
     * Уникальный идентификатор сущности.
     */
    @Id
    lateinit var id: String

}