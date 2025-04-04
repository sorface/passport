package by.sorface.idp.config.security.oauth2

import by.sorface.idp.config.security.backchannel.dispatcher.BackchannelEventDispatcher
import by.sorface.idp.config.security.backchannel.dispatcher.RestBackchannelEventDispatcher
import by.sorface.idp.config.security.backchannel.generator.JwtLogoutGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.web.client.RestTemplate

@Configuration
class BackchannelConfiguration {

    /**
     * Создает и возвращает экземпляр RestTemplate.
     * @return экземпляр RestTemplate.
     */
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    /**
     * Создает и возвращает экземпляр RestBackchannelEventDispatcher.
     * @param restTemplate экземпляр RestTemplate, который будет использоваться в RestBackchannelEventDispatcher.
     * @return экземпляр RestBackchannelEventDispatcher.
     */
    @Bean
    fun restBackchannelEventDispatcher(restTemplate: RestTemplate) : BackchannelEventDispatcher {
        return RestBackchannelEventDispatcher(restTemplate)
    }

    @Bean
    fun jwtLogoutGenerator(jwtEncoder: JwtEncoder, jwtDecoder: JwtDecoder) = JwtLogoutGenerator(jwtEncoder, jwtDecoder)

}