/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
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
                .allowedMethods(splitAndTrim(propertiesCors.getAllowedMethods()))
                .allowedHeaders(splitAndTrim(propertiesCors.getAllowedHeaders()))
                .allowedOriginPatterns(splitAndTrim(propertiesCors.getAllowedOriginPatterns()))
                .allowCredentials(propertiesCors.isAllowCredentials());
    }

    private String[] splitAndTrim(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new String[0];
        }
        String[] parts = value.split(",");
        int count = 0;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
            if (!parts[i].isEmpty()) {
                parts[count++] = parts[i];
            }
        }
        if (count == parts.length) {
            return parts;
        }
        String[] result = new String[count];
        System.arraycopy(parts, 0, result, 0, count);
        return result;
    }
}
