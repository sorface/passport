package by.devpav.kotlin.oidcidp.service.impl

import by.devpav.kotlin.oidcidp.dao.sql.model.client.RegisteredClientModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.client.JdbcRegisteredClientRepository
import by.devpav.kotlin.oidcidp.graphql.services.ApplicationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultApplicationService(private val jdbcRegisteredClientRepository: JdbcRegisteredClientRepository) : ApplicationService {

    @Transactional(readOnly = true)
    override fun getAll(): List<RegisteredClientModel> = jdbcRegisteredClientRepository.findAll()

    @Transactional(readOnly = true)
    override fun getAllByUser(id: UUID): List<RegisteredClientModel> = jdbcRegisteredClientRepository.findByCreatedUser(id)

}