/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.cxf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * @author rajakolli
 * 
 * Important: Including jackson-jaxrs-json-provider dependency along with a JacksonJsonProvider bean 
 * fixed ERROR JAXRSUtils:1793 - 
 * No message body writer has been found for class java.util.ArrayList, ContentType: application/json
 *
 */
@Configuration
public class ProvidersConfig {

  @Bean
  public JacksonJsonProvider jsonProvider() {
    return new JacksonJsonProvider();
  }
}
