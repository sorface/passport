package by.devpav.kotlin.oidcidp.extencions

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

fun HttpServletRequest.isNotAPI(contextPath: String = ""): Boolean {
    return !this.isAPI(contextPath)
}

fun HttpServletRequest.toIndexPage(): HttpServletRequestWrapper {
    return object : HttpServletRequestWrapper(this) {
        override fun getRequestURI(): String {
            return "$contextPath/index.html"
        }
    }
}