package by.sorface.passport.web.config;

import brave.sampler.Sampler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
@EnableRedisRepositories(basePackages = "by.sorface.passport.web.dao.nosql.redis")
public class SorfaceConfiguration {

    public static final String I18_BUNDLE_LOCATION = "language/messages";

    /**
     * The resourceBundleMessageSource function creates a ResourceBundleMessageSource bean.
     * This bean is used to resolve messages in the application, such as error messages.
     * The resource bundle location is set to &quot;i18n/messages&quot;.
     *
     * @return A resourcebundlemessagesource object
     */
    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        final var source = new ResourceBundleMessageSource();
        {
            source.setBasename(I18_BUNDLE_LOCATION);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        }

        return source;
    }

    /**
     * The userAgentAnalyzer function creates a new UserAgentAnalyzer object.
     * The UserAgentAnalyzer class is part of the ua-parser library, which parses user agent strings into their component parts.
     * <p>
     * This function uses the builder pattern to create a new instance of this class with some custom settings:
     * - hideMatcherLoadStats() hides log messages that are printed when the parser loads its data files (which happens on startup).
     * - withCache(10000) sets up an in-memory cache for parsed user agents, so that parsing doesn't have to happen every time.
     *
     * @return A useragentanalyzer object
     */
    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
