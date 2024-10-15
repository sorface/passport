package by.sorface.passport.web.config;

import by.sorface.passport.web.dao.auditors.SecurityAuditorAware;
import by.sorface.passport.web.dao.models.UserEntity;
import by.sorface.passport.web.services.users.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfiguration {

    /**
     * The auditorProvider function is used to provide the AuditorAware interface with a UserEntity object.
     * This allows us to use Spring Data JPA's
     *
     * @param userService Get the current logged in user
     * @return A securityauditoraware object, which is a class that implements the auditoraware interface
     */
    @Bean
    public AuditorAware<UserEntity> auditorProvider(final UserService userService) {
        return new SecurityAuditorAware(userService);
    }

}
