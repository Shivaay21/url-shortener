package com.example.urlshortner.config;

import com.example.urlshortner.util.RateLimiterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RateLimiterFilter rateLimiterFilter) throws Exception{
        http.
                csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        http.addFilterBefore(rateLimiterFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
