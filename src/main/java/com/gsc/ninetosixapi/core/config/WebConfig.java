package com.gsc.ninetosixapi.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.mvc.cors.mapping}")
    private String corsMapping;

    @Value("${spring.mvc.cors.allow-credentials}")
    private boolean corsAllowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsMapping)
                .allowedOrigins("https://www.ninetosixapi.tk","https://deogus-dev.github.io","http://localhost:7889")
                .allowCredentials(corsAllowCredentials)
                .allowedMethods(
                    HttpMethod.GET.name(),
                    HttpMethod.HEAD.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name());

    }
}
