package by.sorface.passport.web.config

import brave.sampler.Sampler
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import java.nio.charset.StandardCharsets

@Configuration
@EnableConfigurationProperties
open class SorfaceConfiguration {

    @Bean
    open fun resourceBundleMessageSource(): ResourceBundleMessageSource {
        val source = ResourceBundleMessageSource()
        source.setBasename(I18_BUNDLE_LOCATION)
        source.setDefaultEncoding(StandardCharsets.UTF_8.name())

        return source
    }

    @Bean
    open fun userAgentAnalyzer(): UserAgentAnalyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build()

    @Bean
    open fun defaultSampler(): Sampler = Sampler.ALWAYS_SAMPLE

    companion object {
        const val I18_BUNDLE_LOCATION: String = "language/messages"
    }
}
