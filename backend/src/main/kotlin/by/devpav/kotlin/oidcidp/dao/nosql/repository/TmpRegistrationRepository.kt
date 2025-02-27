package by.devpav.kotlin.oidcidp.dao.nosql.repository

import by.devpav.kotlin.oidcidp.dao.nosql.model.TmpRegistration
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import org.springframework.stereotype.Repository

@Repository
interface TmpRegistrationRepository : CrudRepository<TmpRegistration, String>, QueryByExampleExecutor<TmpRegistration>