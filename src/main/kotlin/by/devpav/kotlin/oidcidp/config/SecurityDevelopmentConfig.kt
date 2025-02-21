package by.devpav.kotlin.oidcidp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = true)
@Profile("development")
class SecurityDevelopmentConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests.requestMatchers("/h2-console/**", "**.css", "**.js", "**,jsp", "/graphiql/**", "/graphql/**").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .formLogin(Customizer.withDefaults())

        return http.build()
    }

}


@EnableMethodSecurity
@Configuration(proxyBeanMethods = true)
@EnableWebSecurity(debug = true)
@Profile("production")
class SecurityProductionConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeRequests -> authorizeRequests.anyRequest().authenticated() }
            .csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .formLogin(Customizer.withDefaults())

        return http.build()
    }

}
