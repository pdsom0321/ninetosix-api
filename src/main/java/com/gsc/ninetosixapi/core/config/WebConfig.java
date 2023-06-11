package com.gsc.ninetosixapi.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.mvc.cors.mapping}")
    private String corsMapping;

    @Value("${spring.mvc.cors.allowed-origins}")
    private String corsAllowedOrigins;

    @Value("${spring.mvc.cors.allow-credentials}")
    private boolean corsAllowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsMapping)
                .allowedOrigins(corsAllowedOrigins)
                .allowCredentials(corsAllowCredentials);
    }
}
