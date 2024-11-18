package by.sorface.passport.web.config.options.locale

import by.sorface.passport.web.constants.SupportedLocales
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpMethod

@ExtendWith(MockitoExtension::class)
@EnableConfigurationProperties(LocaleOptions::class)
internal class LocaleOptionsTest {
    @Mock
    private val localeOptions: LocaleOptions? = null

    @InjectMocks
    private val localeOptionsTest: LocaleOptionsTest? = null

    @Test
    fun testGetDefaultLocale() {
        Mockito.`when`(localeOptions!!.defaultLocale).thenReturn(SupportedLocales.EN.locale)
        Assertions.assertEquals(SupportedLocales.EN, localeOptionsTest!!.localeOptions!!.defaultLocale)
    }

    @Test
    fun testGetChangeLocaleParameterName() {
        Mockito.`when`(localeOptions!!.changeLocaleParameterName).thenReturn("newLang")
        Assertions.assertEquals("newLang", localeOptionsTest!!.localeOptions!!.changeLocaleParameterName)
    }

    @Test
    fun testGetChangeLocaleMethods() {
        val methods = arrayOf(HttpMethod.GET, HttpMethod.POST)
        Mockito.`when`(localeOptions!!.changeLocaleMethods).thenReturn(methods)
        Assertions.assertArrayEquals(methods, localeOptionsTest!!.localeOptions!!.changeLocaleMethods)
    }

    @Test
    fun testGetCookie() {
        Mockito.`when`(localeOptions!!.cookie).thenReturn(LocaleCookieOptions())
        Assertions.assertEquals(LocaleCookieOptions(), localeOptionsTest!!.localeOptions!!.cookie)
    }
}

