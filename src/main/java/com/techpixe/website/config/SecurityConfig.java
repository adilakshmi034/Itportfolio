package com.techpixe.website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity  http) throws Exception 
    {
        http
            // Disable CSRF (Cross-Site Request Forgery) if you don't need it
            .csrf().disable()
            // Permit all requests (disable login page)
            .authorizeRequests()
                .anyRequest().permitAll()
            .and()
            // Disable form-based login
            .formLogin().disable();

        return http.build();
    }
}