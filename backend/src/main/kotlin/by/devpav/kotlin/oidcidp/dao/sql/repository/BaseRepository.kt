package by.devpav.kotlin.oidcidp.dao.sql.repository

import by.devpav.kotlin.oidcidp.dao.sql.model.BaseModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : BaseModel> : JpaRepository<T, UUID>
