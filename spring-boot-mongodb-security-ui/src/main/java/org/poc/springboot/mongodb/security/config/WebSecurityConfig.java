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

package org.poc.springboot.mongodb.security.config;

import org.poc.springboot.mongodb.security.service.CustomUserDetailsServiceImpl;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Custom WebSecurityConfigurerAdapter.
 *
 * @author Raja Kolli
 * @since 0.2.1
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

	private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder,
			CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler,
			CustomUserDetailsServiceImpl customUserDetailsServiceImpl) {
		super();
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
		this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
	}

	@Bean
	public UserDetailsService mongoUserDetails() {
		return this.customUserDetailsServiceImpl;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService userDetailsService = mongoUserDetails();
		auth.userDetailsService(userDetailsService).passwordEncoder(this.bCryptPasswordEncoder);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/").permitAll()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().antMatchers("/login")
				.permitAll().antMatchers("/signup").permitAll().antMatchers("/dashboard/**").hasAuthority("ADMIN")
				.anyRequest().authenticated().and().csrf().disable().formLogin()
				.successHandler(this.customizeAuthenticationSuccessHandler).loginPage("/login")
				.failureUrl("/login?error=true").usernameParameter("email").passwordParameter("password").and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
				.exceptionHandling();
	}

}
