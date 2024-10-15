package by.sorface.passport.web.services.locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocaleI18ServiceTest {

    @Mock
    private ResourceBundleMessageSource messageSource;

    @InjectMocks
    private LocaleI18Service localeI18Service;

    @Test
    void testGetI18Message() {
        final var i18Code = "test.i18.code";
        final var expectedMessage = "Test message";
        when(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        final var result = localeI18Service.getI18Message(i18Code);

        assertEquals(expectedMessage, result);
    }

    @Test
    void testGetI18MessageWithArgs() {
        final var i18Code = "test.i18.code";
        final var expectedMessage = "Test message with {arg1}";
        final var args = new HashMap<String, String>() {{
            put("arg1", "value1");
        }};
        when(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        final var result = localeI18Service.getI18Message(i18Code, args);

        assertEquals("Test message with value1", result);
    }

    @Test
    void testGetI18MessageWithNullArgs() {
        final var i18Code = "test.i18.code";
        final var expectedMessage = "Test message";
        when(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        final var result = localeI18Service.getI18Message(i18Code, null);

        assertEquals(expectedMessage, result);
    }

    @Test
    void testGetI18MessageWithEmptyArgs() {
        final var i18Code = "test.i18.code";
        final var expectedMessage = "Test message";
        when(messageSource.getMessage(i18Code, null, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        final var result = localeI18Service.getI18Message(i18Code, new HashMap<>());

        assertEquals(expectedMessage, result);
    }
}
