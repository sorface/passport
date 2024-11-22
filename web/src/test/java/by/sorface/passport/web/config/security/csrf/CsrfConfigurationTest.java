package by.sorface.passport.web.config.security.csrf;

import by.sorface.passport.web.config.options.CookieProperties;
import by.sorface.passport.web.security.csrf.CsrfConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CsrfConfigurationTest {

    private final CsrfConfiguration csrfConfiguration = new CsrfConfiguration();

    @Test
    public void cookieCsrfTokenRepository() {
        final var csrfCookieOptions = new CookieProperties.CsrfCookieOptions();
        {
            csrfCookieOptions.setDomain("localhost");
            csrfCookieOptions.setName("csrf-cookie-name");
            csrfCookieOptions.setHttpOnly(true);
            csrfCookieOptions.setPath("/");
        }

        final var cookieOptions = new CookieProperties();
        cookieOptions.setCsrf(csrfCookieOptions);

        final var cookieCsrfTokenRepository = csrfConfiguration.cookieCsrfTokenRepository(cookieOptions);

        Assertions.assertNotNull(cookieCsrfTokenRepository);
        Assertions.assertEquals(csrfCookieOptions.getPath(), cookieCsrfTokenRepository.getCookiePath());
    }

}