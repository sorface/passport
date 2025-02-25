package by.devpav.kotlin.oidcidp.dao.sql.auditors

import by.devpav.kotlin.oidcidp.dao.sql.model.UserModel
import by.devpav.kotlin.oidcidp.dao.sql.repository.user.UserRepository
import by.devpav.kotlin.oidcidp.extencions.getPrincipalIdOrNull
import by.devpav.kotlin.oidcidp.records.SorfacePrincipal
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

/**
 * Компонент SecurityAuditorAware реализует интерфейс AuditorAware<UserModel> и предоставляет информацию о текущем аудиторе.
 * Он используется для автоматического заполнения полей аудита в сущностях, таких как создатель и редактор.
 *
 * @param userRepository репозиторий для работы с пользователями.
 */
@Component
class SecurityAuditorAware(private val userRepository: UserRepository) : AuditorAware<UserModel> {

    /**
     * Метод getCurrentAuditor возвращает текущего аудитора в виде объекта UserModel.
     * Он извлекает идентификатор пользователя из контекста безопасности и использует его для поиска пользователя в репозитории.
     *
     * @return объект UserModel или пустой Optional, если аудитор не найден.
     */
    override fun getCurrentAuditor(): Optional<UserModel> {
        val principalId = SecurityContextHolder.getContext().getPrincipalIdOrNull()

        principalId ?: return Optional.empty()

        return userRepository.findById(principalId)
    }
}
