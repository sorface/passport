package by.sorface.passport.web.converters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

public class OAuth2TwitchUserRequestEntityConverter extends OAuth2UserRequestEntityConverter {

    /**
     * The convert function is used to convert the OAuth2UserRequest into a RequestEntity.
     * The RequestEntity will be used by the RestTemplate to make an HTTP request for user information.
     * This function sets up the headers and URI of that request, which are then passed back to Spring Security's OAuth2LoginAuthenticationProvider class.
     *
     * @param userRequest Get the client registration and access token
     * @return A requestentity which is then used to retrieve the user's information
     */
    @Override
    public RequestEntity<?> convert(final OAuth2UserRequest userRequest) {
        final var clientRegistration = userRequest.getClientRegistration();

        final var userInfoUri = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri();
        final URI uri = UriComponentsBuilder.fromUriString(userInfoUri).build().toUri();

        final var headers = new HttpHeaders();
        {
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
            headers.set(TwitchHeaderEnum.CLIENT_ID.getName(), clientRegistration.getClientId());
        }

        return new RequestEntity<>(headers, HttpMethod.GET, uri);
    }

    @Getter
    @RequiredArgsConstructor
    private enum TwitchHeaderEnum {
        CLIENT_ID("Client-ID");

        private final String name;
    }

}
