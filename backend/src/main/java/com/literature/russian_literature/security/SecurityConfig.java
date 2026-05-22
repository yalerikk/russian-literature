package com.literature.russian_literature.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // ----- permitAll -----
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/catalog/categories/active", "/api/catalog/categories/code/*",
                                "/api/ratings/book/*/summary", "/books/{id}/files", "/genres", "/tags", "/tags/by-type", "/tags/by-type/**",
                                "/authors/**").permitAll()
                        // ----- authenticated -----
                        .requestMatchers("/api/ratings/**", "/users/me/**", "/books/*/read", "/books/*/download").authenticated()
                        // ----- ADMIN -----
                        .requestMatchers(HttpMethod.GET, "/books/admin/list", "/authors/admin/list", "/users/admin/list",
                                "/genres/admin/list", "/tags/admin/list", "/api/catalog/categories/admin/list",
                                "/api/catalog/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/**", "/authors/**", "/genres/**", "/tags/**",
                                "/api/catalog/categories/**", "/api/images/upload/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/books/**", "/authors/**", "/genres/**", "/tags/**",
                                "/api/catalog/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**", "/authors/**", "/genres/**", "/tags/**",
                                "/api/catalog/categories/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
