/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Defines WebSecurity Configuration.
 *
 * @author Raja Kolli
 *
 */
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(
				User.withUsername("username").password("{noop}password").authorities("ROLE_USER").build(),
				User.withUsername("admin").password("{noop}admin")
						.authorities("ROLE_ACTUATOR", "ROLE_USER", "ROLE_ADMIN").build());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().requestMatchers(EndpointRequest.to("health", "info")).permitAll()
				.requestMatchers(EndpointRequest.toAnyEndpoint().excluding(MappingsEndpoint.class)).hasRole("ACTUATOR")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN").antMatchers("/**").hasRole("USER").and()
				.httpBasic();
		// H2 database console runs inside a frame, So we need to disable X-Frame-Options
		// in Spring Security.
		http.headers().frameOptions().disable();
	}

}
