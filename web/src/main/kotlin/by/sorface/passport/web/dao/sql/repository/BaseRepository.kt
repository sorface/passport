package by.sorface.passport.web.dao.sql.repository

import by.sorface.passport.web.dao.sql.models.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : BaseEntity?> : JpaRepository<T, UUID?>
