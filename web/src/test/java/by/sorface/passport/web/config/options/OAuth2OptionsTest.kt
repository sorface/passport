package by.sorface.passport.web.config.options

import by.sorface.passport.web.config.options.OAuth2Options.RedisOptions
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
@EnableConfigurationProperties(OAuth2Options::class)
internal class OAuth2OptionsTest {
    @Mock
    private val oAuth2Options: OAuth2Options? = null

    @InjectMocks
    private val oAuth2OptionsTest: OAuth2OptionsTest? = null

    @Test
    fun testGetIssuerUrl() {
        Mockito.`when`(oAuth2Options!!.issuerUrl).thenReturn("testUrl")
        Assertions.assertEquals("testUrl", oAuth2OptionsTest!!.oAuth2Options!!.issuerUrl)
    }

    @Test
    fun testGetRedis() {
        Mockito.`when`(oAuth2Options!!.redis).thenReturn(RedisOptions())
        Assertions.assertEquals(RedisOptions(), oAuth2OptionsTest!!.oAuth2Options!!.redis)
    }
}


