package by.sorface.passport.web.config.options.locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(LocaleCookieOptions.class)
class LocaleCookieOptionsTest {

    @Mock
    private LocaleCookieOptions localeCookieOptions;

    @InjectMocks
    private LocaleCookieOptionsTest localeCookieOptionsTest;

    @Test
    void testGetName() {
        when(localeCookieOptions.getName()).thenReturn("testName");
        assertEquals("testName", localeCookieOptionsTest.localeCookieOptions.getName());
    }

    @Test
    void testGetDomain() {
        when(localeCookieOptions.getDomain()).thenReturn("testDomain");
        assertEquals("testDomain", localeCookieOptionsTest.localeCookieOptions.getDomain());
    }

    @Test
    void testGetPath() {
        when(localeCookieOptions.getPath()).thenReturn("testPath");
        assertEquals("testPath", localeCookieOptionsTest.localeCookieOptions.getPath());
    }

    @Test
    void testIsHttpOnly() {
        when(localeCookieOptions.isHttpOnly()).thenReturn(true);
        assertTrue(localeCookieOptionsTest.localeCookieOptions.isHttpOnly());
    }
}
