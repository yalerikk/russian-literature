package com.literature.russian_literature.errors;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,        // Понятное сообщение для пользователя
        String details,        // Детали для разработчика
        LocalDateTime timestamp
) {
    public ErrorResponse(String message, String details) {
        this(message, details, LocalDateTime.now());
    }
}