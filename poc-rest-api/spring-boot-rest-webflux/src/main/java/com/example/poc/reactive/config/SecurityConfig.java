/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        var postPath = "/posts/**";
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(
                        httpBasicSpec ->
                                httpBasicSpec.securityContextRepository(
                                        NoOpServerSecurityContextRepository.getInstance()))
                .authorizeExchange(
                        it ->
                                it.pathMatchers(HttpMethod.GET, "/", postPath)
                                        .permitAll()
                                        .pathMatchers(HttpMethod.DELETE, postPath)
                                        .hasRole("ADMIN")
                                        .pathMatchers(postPath)
                                        .hasRole("USER")
                                        .pathMatchers("/users/{user}/**")
                                        .access(
                                                (authentication, context) ->
                                                        authentication
                                                                .map(
                                                                        auth ->
                                                                                context.getVariables()
                                                                                        .get("user")
                                                                                        .equals(
                                                                                                auth
                                                                                                        .getName()))
                                                                .map(AuthorizationDecision::new))
                                        .anyExchange()
                                        .authenticated())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user =
                User.withUsername("user")
                        .passwordEncoder(passwordEncoder::encode)
                        .password("password")
                        .roles("USER")
                        .build();
        UserDetails admin =
                User.withUsername("admin")
                        .passwordEncoder(passwordEncoder::encode)
                        .password("password")
                        .roles("USER", "ADMIN")
                        .build();
        log.info("user: {}", user);
        log.info("admin: {}", admin);
        return new MapReactiveUserDetailsService(user, admin);
    }
}
