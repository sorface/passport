package by.sorface.idp.extencions

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun HttpServletRequest.isHtmlRequest(): Boolean {
    val acceptHeader = this.getHeader(HttpHeaders.ACCEPT)
    return acceptHeader != null && acceptHeader.contains(MediaType.TEXT_HTML_VALUE)
}

fun HttpServletRequest.isAPI(contextPath: String = ""): Boolean {
    return this.requestURI.startsWith("$contextPath/api")
}

fun HttpServletRequest.isOAuth2API(contextPath: String = ""): Boolean {
    return setOf("/oauth2", "/connect/register", "/userinfo", "/connect/logout").any {
        this.requestURI.startsWith("$contextPath$it")
    }
}

fun HttpServletRequest.isNotAPI(contextPath: String = ""): Boolean {
    return !this.isAPI(contextPath)
}

fun HttpServletRequest.isNotOAuth2API(contextPath: String = ""): Boolean {
    return !this.isOAuth2API(contextPath)
}

fun HttpServletRequest.toIndexPage(): HttpServletRequestWrapper {
    return object : HttpServletRequestWrapper(this) {
        override fun getRequestURI(): String {
            return "$contextPath/index.html"
        }
    }
}

fun HttpServletRequest.findCookieByName(name: String): Cookie? {
    return this.cookies.find { it.name == name }
}

fun HttpServletRequest.findCookieValueByName(name: String): String? {
    return this.findCookieByName(name)?.value
}
