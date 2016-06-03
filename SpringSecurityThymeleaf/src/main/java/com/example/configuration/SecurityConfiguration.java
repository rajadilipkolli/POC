package com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/resources/**", "/webjars/**");
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        
        // static resources
//        httpSecurity.authorizeRequests()
//        .antMatchers("/css/**", "/js/**", "/images/**", "/resources/**", "/webjars/**").permitAll();
        
        httpSecurity.authorizeRequests()
                        .antMatchers("/login","/user/create").anonymous()
//                        .anyRequest().authenticated()
                        .antMatchers("/favicon.ico").permitAll()
                        .anyRequest().fullyAuthenticated()
                        .and()
                    .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/login-process.html")
                        .failureUrl("/login?error")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .and()
                    .logout()
                        .deleteCookies("remember-me")
                        .logoutSuccessUrl("/login?logout")
                    .and().rememberMe();
        
        httpSecurity.exceptionHandling().accessDeniedPage("/404.html");
        httpSecurity.sessionManagement().invalidSessionUrl("/login");  
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
