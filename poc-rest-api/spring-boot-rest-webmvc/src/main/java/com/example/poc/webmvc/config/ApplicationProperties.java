/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Setter
@Getter
@ConfigurationProperties("application")
public class ApplicationProperties {

    @NestedConfigurationProperty private Cors cors = new Cors();

    @Setter
    @Getter
    public static class Cors {
        private String pathPattern = "/api/**";
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";
        private String allowedHeaders = "Content-Type,Authorization,X-Requested-With,Accept,Origin";
        private String allowedOriginPatterns = "*";
        private boolean allowCredentials = true;

        public String[] getAllowedMethodsArray() {
            return allowedMethods.split(",");
        }

        public String[] getAllowedHeadersArray() {
            return allowedHeaders.split(",");
        }

        public String[] getAllowedOriginPatternsArray() {
            return allowedOriginPatterns.split(",");
        }
    }
}
