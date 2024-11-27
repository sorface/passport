package by.sorface.passport.web.security.config.formlogin

import by.sorface.passport.web.security.AccountCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JsonUsernamePasswordAuthenticationFilter : UsernamePasswordAuthenticationFilter() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(JsonUsernamePasswordAuthenticationFilter::class.java)
    }

    private val mapper = ObjectMapper()

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        if (HttpMethod.POST.matches(request.method).not()) {
            throw AuthenticationServiceException("Authentication method not supported: " + request.method)
        }

        if (MediaType.APPLICATION_JSON_VALUE.equals(request.contentType, ignoreCase = true).not()) {
            throw AuthenticationServiceException("Authentication method not supported application/json: " + request.contentType)
        }

        val authentication: UsernamePasswordAuthenticationToken?

        try {
            val accountCredentials: AccountCredentials = mapper.readValue(request.reader, AccountCredentials::class.java)

            logger.info("authentication user by username [${accountCredentials.username}] and password [*****]")

            authentication = UsernamePasswordAuthenticationToken(accountCredentials.username, accountCredentials.password)
        } catch (e: java.lang.Exception) {
            throw InternalAuthenticationServiceException("Failed to parse authentication request body")
        }

        return super.getAuthenticationManager().authenticate(authentication);
    }

}