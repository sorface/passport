package by.sorface.passport.web.config.options

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.context.properties.EnableConfigurationProperties

// Import necessary libraries

@ExtendWith(MockitoExtension::class)
@EnableConfigurationProperties(EndpointOptions::class)
internal class EndpointOptionsTest {
    @Mock
    private val endpointOptions: EndpointOptions? = null

    @InjectMocks
    private val endpointOptionsTest: EndpointOptionsTest? = null

    @Test
    fun testGetUriPageSignIn() {
        Mockito.`when`(endpointOptions!!.uriPageSignIn).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriPageSignIn)
    }

    @Test
    fun testGetUriPageSignUp() {
        Mockito.`when`(endpointOptions!!.uriPageSignUp).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriPageSignUp)
    }

    @Test
    fun testGetUriPageProfile() {
        Mockito.`when`(endpointOptions!!.uriPageProfile).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriPageProfile)
    }

    @Test
    fun testGetUriPageFailure() {
        Mockito.`when`(endpointOptions!!.uriPageFailure).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriPageFailure)
    }

    @Test
    fun testGetUriPageNotFound() {
        Mockito.`when`(endpointOptions!!.uriPageNotFound).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriPageNotFound)
    }

    @Test
    fun testGetUriApiLogin() {
        Mockito.`when`(endpointOptions!!.uriApiLogin).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriApiLogin)
    }

    @Test
    fun testGetUriApiLogout() {
        Mockito.`when`(endpointOptions!!.uriApiLogout).thenReturn("testUri")
        Assertions.assertEquals("testUri", endpointOptionsTest!!.endpointOptions!!.uriApiLogout)
    }
}

