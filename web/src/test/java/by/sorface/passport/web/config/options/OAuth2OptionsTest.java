package by.sorface.passport.web.config.options;// Import necessary libraries

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(OAuth2Options.class)
class OAuth2OptionsTest {

    @Mock
    private OAuth2Options oAuth2Options;

    @InjectMocks
    private OAuth2OptionsTest oAuth2OptionsTest;

    @Test
    void testGetIssuerUrl() {
        when(oAuth2Options.getIssuerUrl()).thenReturn("testUrl");
        assertEquals("testUrl", oAuth2OptionsTest.oAuth2Options.getIssuerUrl());
    }

    @Test
    void testGetRedis() {
        when(oAuth2Options.getRedis()).thenReturn(new OAuth2Options.RedisOptions());
        assertEquals(new OAuth2Options.RedisOptions(), oAuth2OptionsTest.oAuth2Options.getRedis());
    }
}


