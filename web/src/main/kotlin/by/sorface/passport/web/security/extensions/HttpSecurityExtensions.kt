package by.sorface.passport.web.security.extensions

import by.sorface.passport.web.security.config.formlogin.JsonFormLoginConfigurer
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity

fun HttpSecurity.jsonLogin(jsonLoginCustomizer: Customizer<JsonFormLoginConfigurer<HttpSecurity>>): HttpSecurity {
    this.with(JsonFormLoginConfigurer(), jsonLoginCustomizer)
    return this
}
