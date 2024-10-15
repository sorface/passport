package by.sorface.passport.web.config.security;

import by.sorface.passport.web.config.options.CorsOptions;
import by.sorface.passport.web.config.options.locale.LocaleCookieOptions;
import by.sorface.passport.web.config.options.locale.LocaleOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CorsOptions corsOptions;

    private final LocaleOptions localeOptions;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        corsOptions.getOptions().forEach(it -> {
            registry.addMapping(it.getPattern())
                    .allowedOrigins(it.getAllowedOrigins())
                    .allowedMethods(it.getAllowedMethods())
                    .allowedHeaders(it.getAllowedHeaders())
                    .exposedHeaders(it.getExposedHeaders())
                    .allowCredentials(it.isAllowCredentials())
                    .maxAge(it.getMaxAge());
        });
    }

    @Bean
    public LocaleResolver localeResolver() {
        final LocaleCookieOptions cookieOptions = localeOptions.getCookie();

        final var cookieLocaleResolver = new CookieLocaleResolver(cookieOptions.getName());

        cookieLocaleResolver.setCookieDomain(cookieOptions.getDomain());
        cookieLocaleResolver.setCookiePath(cookieOptions.getPath());
        cookieLocaleResolver.setDefaultLocale(localeOptions.getDefaultLocale());
        cookieLocaleResolver.setCookieHttpOnly(false);
        cookieLocaleResolver.setCookieSecure(true);

        return cookieLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        final var localeChangeInterceptor = new LocaleChangeInterceptor();

        final var httpMethods = Arrays.stream(localeOptions.getChangeLocaleMethods())
                .map(HttpMethod::name)
                .toArray(String[]::new);

        localeChangeInterceptor.setParamName(localeOptions.getChangeLocaleParameterName());
        localeChangeInterceptor.setHttpMethods(httpMethods);

        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
