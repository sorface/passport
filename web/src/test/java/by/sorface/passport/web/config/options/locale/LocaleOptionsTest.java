package by.sorface.passport.web.config.options.locale;

import by.sorface.passport.web.constants.SupportedLocales;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(LocaleOptions.class)
class LocaleOptionsTest {

    @Mock
    private LocaleOptions localeOptions;

    @InjectMocks
    private LocaleOptionsTest localeOptionsTest;

    @Test
    void testGetDefaultLocale() {
        when(localeOptions.getDefaultLocale()).thenReturn(SupportedLocales.EN);
        assertEquals(SupportedLocales.EN, localeOptionsTest.localeOptions.getDefaultLocale());
    }

    @Test
    void testGetChangeLocaleParameterName() {
        when(localeOptions.getChangeLocaleParameterName()).thenReturn("newLang");
        assertEquals("newLang", localeOptionsTest.localeOptions.getChangeLocaleParameterName());
    }

    @Test
    void testGetChangeLocaleMethods() {
        HttpMethod[] methods = new HttpMethod[]{HttpMethod.GET, HttpMethod.POST};
        when(localeOptions.getChangeLocaleMethods()).thenReturn(methods);
        assertArrayEquals(methods, localeOptionsTest.localeOptions.getChangeLocaleMethods());
    }

    @Test
    void testGetCookie() {
        when(localeOptions.getCookie()).thenReturn(new LocaleCookieOptions());
        assertEquals(new LocaleCookieOptions(), localeOptionsTest.localeOptions.getCookie());
    }
}

