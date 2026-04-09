package com.literature.russian_literature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем запросы с фронтенда (изменяйте по мере необходимости)
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",    // Vue dev server
                "http://127.0.0.1:3000",    // Альтернативный адрес
                "http://localhost:5173",    // Vite default port
                "http://127.0.0.1:5173"     // Vite альтернативный
        ));

        // ВАЖНО: Разрешаем OPTIONS метод для preflight запросов
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Разрешаемые заголовки
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Разрешаем куки/авторизацию если нужно
        config.setAllowCredentials(true);

        // Заголовки, доступные клиенту
        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Disposition"
        ));

        // Предзапрос кэшируется 1 час
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}