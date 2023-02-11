package com.manuelcota.chat_rooms.Config;

import com.manuelcota.chat_rooms.Service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// All security configuration for the application.
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Instance of the userDetails Service.
    @Autowired
    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    // Configuration for HTTP security.
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()           // Disables Cross-Site Request Forgery.
                .cors().and()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // All other sites are permitted by everyone.
                )
                .userDetailsService(jpaUserDetailsService)
                .headers(headers -> headers.frameOptions().sameOrigin())
                .build();
    }

    // Creates an BCrypt password encoder.
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}