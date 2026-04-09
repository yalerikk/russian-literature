package com.literature.russian_literature.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            // Предполагаем, что username = email, и у вас есть сервис для поиска User по email
            // Лучше хранить userId в JWT или в SecurityContext
            //return ((CustomUserDetails) userDetails).getId();
            Long currentUserId = 1L;
        }
        throw new IllegalStateException("Не удалось получить ID пользователя");
    }
}
