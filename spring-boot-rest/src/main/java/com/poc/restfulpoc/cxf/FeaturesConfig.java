/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.cxf;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeaturesConfig {

    @Value("${cxf.path}")
    private String basePath;

    @Bean("swagger2Feature")
    public Feature swagger2Feature() {
        final Swagger2Feature result = new Swagger2Feature();
        result.setTitle("Spring Boot + CXF + Swagger Example");
        result.setDescription("Spring Boot + CXF + Swagger Example description");
        result.setBasePath(this.basePath);
        result.setVersion("v1");
        result.setContact("Raja Dilip Kolli");
        result.setSchemes(new String[] { "http", "https" });
        result.setPrettyPrint(true);
        return result;
    }
}
