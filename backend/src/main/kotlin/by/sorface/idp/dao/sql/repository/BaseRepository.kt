package by.sorface.idp.dao.sql.repository

import by.sorface.idp.dao.sql.model.BaseModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : BaseModel> : JpaRepository<T, UUID>
