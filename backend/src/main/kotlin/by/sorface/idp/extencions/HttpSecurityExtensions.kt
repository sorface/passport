package by.sorface.idp.extencions

import by.sorface.idp.config.security.formlogin.JsonFormLoginConfigurer
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity

fun HttpSecurity.jsonLogin(jsonLoginCustomizer: Customizer<JsonFormLoginConfigurer<HttpSecurity>>): HttpSecurity {
    this.with(JsonFormLoginConfigurer(), jsonLoginCustomizer)
    return this
}

fun HttpSecurity.serverBackchannelLogout(jsonLoginCustomizer: Customizer<JsonFormLoginConfigurer<HttpSecurity>>): HttpSecurity {
    this.with(JsonFormLoginConfigurer(), jsonLoginCustomizer)
    return this
}