package com.literature.russian_literature.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        }
        throw new IllegalStateException("Не удалось получить ID пользователя");
    }
}
