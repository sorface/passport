package by.sorface.passport.web.constants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlPatternEnumTest {

    @Test
    void toArray() {
        String[] strings = UrlPatternEnum.toArray(UrlPatternEnum.CSRF);

        Assertions.assertEquals(1, strings.length);
        Assertions.assertEquals(strings[0], UrlPatternEnum.CSRF.getPatterns().stream().findFirst().orElse(null));
    }

}