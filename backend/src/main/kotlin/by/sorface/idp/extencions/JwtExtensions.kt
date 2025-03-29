package by.sorface.idp.extencions

import org.springframework.security.oauth2.jwt.Jwt

fun Jwt.clientId() : String? = this.claims["azp"] as String?

fun Jwt.sessionId() : String? = this.claims["sid"] as String?