/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
class WebMvcConfig implements WebMvcConfigurer {
    private final ApplicationProperties applicationProperties;

    WebMvcConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        ApplicationProperties.Cors propertiesCors = applicationProperties.getCors();
        registry.addMapping(propertiesCors.getPathPattern())
                .allowedMethods(propertiesCors.getAllowedMethods().split(","))
                .allowedHeaders(propertiesCors.getAllowedHeaders().split(","))
                .allowedOriginPatterns(propertiesCors.getAllowedOriginPatterns().split(","))
                .allowCredentials(propertiesCors.isAllowCredentials());
    }
}
