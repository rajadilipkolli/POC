package com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@EnableGlobalMethodSecurity(prePostEnabled = true/*, securedEnabled = true*/)
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		/*
		 * httpSecurity.csrf().disable(); httpSecurity.headers().frameOptions().disable();
		 */

		// @formatter:off
		httpSecurity
				.authorizeRequests()
					.antMatchers("/login", "/user/create")
						.permitAll()
					.anyRequest().authenticated()
				.and()
					.formLogin()
						.loginPage("/login")
						.loginProcessingUrl("/login.html")
						.failureUrl("/login?error")
						.defaultSuccessUrl("/home", true)
				.and()
					.logout()
						.deleteCookies("remember-me")
						.logoutSuccessUrl("/login?logout")
				/*.and()
					.rememberMe()*/
				.and()
					.exceptionHandling()
						.accessDeniedPage("/404.html")
				.and()
					.sessionManagement()
						.invalidSessionUrl("/login");
		
		// @formatter:on
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}
}
