package by.sorface.passport.web.config.options;

// Import necessary libraries

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(EndpointOptions.class)
class EndpointOptionsTest {

    @Mock
    private EndpointOptions endpointOptions;

    @InjectMocks
    private EndpointOptionsTest endpointOptionsTest;

    @Test
    void testGetUriPageSignIn() {
        when(endpointOptions.getUriPageSignIn()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriPageSignIn());
    }

    @Test
    void testGetUriPageSignUp() {
        when(endpointOptions.getUriPageSignUp()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriPageSignUp());
    }

    @Test
    void testGetUriPageProfile() {
        when(endpointOptions.getUriPageProfile()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriPageProfile());
    }

    @Test
    void testGetUriPageFailure() {
        when(endpointOptions.getUriPageFailure()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriPageFailure());
    }

    @Test
    void testGetUriPageNotFound() {
        when(endpointOptions.getUriPageNotFound()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriPageNotFound());
    }

    @Test
    void testGetUriApiLogin() {
        when(endpointOptions.getUriApiLogin()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriApiLogin());
    }

    @Test
    void testGetUriApiLogout() {
        when(endpointOptions.getUriApiLogout()).thenReturn("testUri");
        assertEquals("testUri", endpointOptionsTest.endpointOptions.getUriApiLogout());
    }
}

