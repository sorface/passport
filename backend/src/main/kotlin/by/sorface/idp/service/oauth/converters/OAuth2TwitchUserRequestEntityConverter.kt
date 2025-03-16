package by.sorface.idp.service.oauth.converters

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter
import org.springframework.web.util.UriComponentsBuilder

class OAuth2TwitchUserRequestEntityConverter : OAuth2UserRequestEntityConverter() {

    override fun convert(userRequest: OAuth2UserRequest): RequestEntity<*> {
        val clientRegistration = userRequest.clientRegistration

        val userInfoUri = clientRegistration.providerDetails.userInfoEndpoint.uri
        val uri = UriComponentsBuilder.fromUriString(userInfoUri).build().toUri()

        val headers = HttpHeaders()
        run {
            headers.accept = listOf(MediaType.APPLICATION_JSON)
            headers.setBearerAuth(userRequest.accessToken.tokenValue)
            headers[TwitchHeaderEnum.CLIENT_ID.value] = clientRegistration.clientId
        }

        return RequestEntity<Any>(headers, HttpMethod.GET, uri)
    }

    private enum class TwitchHeaderEnum(val value: String) {
        CLIENT_ID("Client-ID");
    }
}
