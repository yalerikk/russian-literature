package com.literature.russian_literature.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/books/**", "/authors/**", "/genres/**", "/tags/**", "/api/catalog/categories/**", "/api/images/upload/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/books/**", "/authors/**", "/genres/**", "/tags/**", "/api/catalog/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**", "/authors/**", "/genres/**", "/tags/**", "/api/catalog/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/ratings/book/*/summary").permitAll()
                        .requestMatchers("/api/ratings/**").authenticated()
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
