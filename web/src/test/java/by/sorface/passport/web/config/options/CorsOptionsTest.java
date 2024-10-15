package by.sorface.passport.web.config.options;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(CorsOptions.class)
class CorsOptionsTest {

    @Mock
    private CorsOptions corsOptions;

    @InjectMocks
    private CorsOptionsTest corsOptionsTest;

    @Test
    void testGetOptions() {
        List<CorsOptions.CorsItemOptions> options = Arrays.asList(
                new CorsOptions.CorsItemOptions(),
                new CorsOptions.CorsItemOptions()
        );
        when(corsOptions.getOptions()).thenReturn(options);
        assertEquals(options, corsOptionsTest.corsOptions.getOptions());
    }
}

