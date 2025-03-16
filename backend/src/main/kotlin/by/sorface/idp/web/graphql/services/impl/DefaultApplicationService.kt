package by.sorface.idp.web.graphql.services.impl

import by.sorface.idp.dao.sql.model.client.RegisteredClientModel
import by.sorface.idp.dao.sql.repository.client.JdbcRegisteredClientRepository
import by.sorface.idp.web.graphql.services.ApplicationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultApplicationService(private val jdbcRegisteredClientRepository: JdbcRegisteredClientRepository) : ApplicationService {

    @Transactional(readOnly = true)
    override fun getAll(): List<RegisteredClientModel> = jdbcRegisteredClientRepository.findAll()

    override fun getAllByUser(id: UUID): List<RegisteredClientModel> = jdbcRegisteredClientRepository.findByCreatedUser(id)

}