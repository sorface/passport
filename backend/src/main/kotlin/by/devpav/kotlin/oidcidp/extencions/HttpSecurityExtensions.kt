package by.devpav.kotlin.oidcidp.extencions

import by.devpav.kotlin.oidcidp.config.security.formlogin.JsonFormLoginConfigurer
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity

fun HttpSecurity.jsonLogin(jsonLoginCustomizer: Customizer<JsonFormLoginConfigurer<HttpSecurity>>): HttpSecurity {
    this.with(JsonFormLoginConfigurer(), jsonLoginCustomizer)
    return this
}