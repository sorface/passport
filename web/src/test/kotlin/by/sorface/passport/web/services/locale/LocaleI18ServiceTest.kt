package by.sorface.passport.web.services.locale

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource

@ExtendWith(MockitoExtension::class)
internal class LocaleI18ServiceTest {

    @Mock
    private lateinit var messageSource: ResourceBundleMessageSource

    @InjectMocks
    private lateinit var localeI18Service: LocaleI18Service

    @Test
    fun testGetI18Message() {
        val i18Code = "test.i18.code"
        val expectedMessage = "Test message"
        Mockito.`when`(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage)

        val result = localeI18Service.getI18Message(i18Code)

        Assertions.assertEquals(expectedMessage, result)
    }

    @Test
    fun testGetI18MessageWithArgs() {
        val i18Code = "test.i18.code"
        val expectedMessage = "Test message with {arg1}"
        val args: HashMap<String, String> = hashMapOf(Pair("arg1", "value1"))

        Mockito.`when`(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage)

        val result = localeI18Service.getI18Message(i18Code, args)

        Assertions.assertEquals("Test message with value1", result)
    }

    @Test
    fun testGetI18MessageWithNullArgs() {
        val i18Code = "test.i18.code"
        val expectedMessage = "Test message"
        Mockito.`when`(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage)

        val result = localeI18Service.getI18Message(i18Code, hashMapOf())

        Assertions.assertEquals(expectedMessage, result)
    }

    @Test
    fun testGetI18MessageWithEmptyArgs() {
        val i18Code = "test.i18.code"
        val expectedMessage = "Test message"
        Mockito.`when`(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage)

        val result = localeI18Service.getI18Message(i18Code, HashMap())

        Assertions.assertEquals(expectedMessage, result)
    }
}
