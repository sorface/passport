package by.sorface.passport.web.config.options

import by.sorface.passport.web.config.options.CorsOptions.CorsItemOptions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.util.*

@ExtendWith(MockitoExtension::class)
@EnableConfigurationProperties(CorsOptions::class)
internal class CorsOptionsTest {
    @Mock
    private val corsOptions: CorsOptions? = null

    @InjectMocks
    private val corsOptionsTest: CorsOptionsTest? = null

    @Test
    fun testGetOptions() {
        val options = Arrays.asList(
            CorsItemOptions(),
            CorsItemOptions()
        )
        Mockito.`when`(corsOptions!!.options).thenReturn(options)
        Assertions.assertEquals(options, corsOptionsTest!!.corsOptions!!.options)
    }
}

