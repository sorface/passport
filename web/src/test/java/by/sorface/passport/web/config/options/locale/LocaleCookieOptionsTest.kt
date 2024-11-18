package by.sorface.passport.web.config.options.locale

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ExtendWith(MockitoExtension::class)
@EnableConfigurationProperties(LocaleCookieOptions::class)
internal class LocaleCookieOptionsTest {

    @Mock
    private lateinit var localeCookieOptions: LocaleCookieOptions

    @InjectMocks
    private lateinit var localeCookieOptionsTest: LocaleCookieOptionsTest

    @Test
    fun testGetName() {
        Mockito.`when`(localeCookieOptions.name).thenReturn("testName")
        Assertions.assertEquals("testName", localeCookieOptionsTest.localeCookieOptions.name)
    }

    @Test
    fun testGetDomain() {
        Mockito.`when`(localeCookieOptions.domain).thenReturn("testDomain")
        Assertions.assertEquals("testDomain", localeCookieOptionsTest.localeCookieOptions.domain)
    }

    @Test
    fun testGetPath() {
        Mockito.`when`(localeCookieOptions.path).thenReturn("testPath")
        Assertions.assertEquals("testPath", localeCookieOptionsTest.localeCookieOptions.path)
    }

    @Test
    fun testIsHttpOnly() {
        Mockito.`when`(localeCookieOptions.httpOnly).thenReturn(true)
        Assertions.assertTrue(localeCookieOptionsTest.localeCookieOptions.httpOnly)
    }

}
