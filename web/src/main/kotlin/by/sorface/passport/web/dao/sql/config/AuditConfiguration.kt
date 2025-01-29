package by.sorface.passport.web.dao.sql.config

import by.sorface.passport.web.dao.sql.auditors.SecurityAuditorAware
import by.sorface.passport.web.dao.sql.models.UserEntity
import by.sorface.passport.web.services.users.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
open class AuditConfiguration {

    @Bean
    open fun auditorProvider(userService: UserService): AuditorAware<UserEntity> = SecurityAuditorAware(userService)

}
