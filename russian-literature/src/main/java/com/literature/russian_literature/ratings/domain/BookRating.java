package com.literature.russian_literature.ratings.domain;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record BookRating(
        @Null Long id,

        @NotNull(message = "ID книги обязателен")
        Long bookId,

        @NotNull(message = "ID пользователя обязателен")
        Long userId,

        @NotNull(message = "Оценка обязательна")
        @Min(value = 1, message = "Оценка должна быть от 1 до 5")
        @Max(value = 5, message = "Оценка должна быть от 1 до 5")
        Integer rating,

        // created и updated не передаются от клиента
        @Null LocalDateTime createdAt,
        @Null LocalDateTime updatedAt
) {

}