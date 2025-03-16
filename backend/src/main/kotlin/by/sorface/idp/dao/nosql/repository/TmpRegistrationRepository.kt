package by.sorface.idp.dao.nosql.repository

import by.sorface.idp.dao.nosql.model.TmpRegistration
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import org.springframework.stereotype.Repository

@Repository
interface TmpRegistrationRepository : CrudRepository<TmpRegistration, String>, QueryByExampleExecutor<TmpRegistration>