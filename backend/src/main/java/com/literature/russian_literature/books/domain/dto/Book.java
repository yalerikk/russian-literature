package com.literature.russian_literature.books.domain.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

public record Book(
        @Null Long id,

        @NotBlank(message = "Название книги обязательно")
        @Size(min = 1, max = 255, message = "Название должно быть от 1 до 255 символов")
        String title,

        @NotNull(message = "Год публикации обязателен")
        @Min(value = 1500, message = "Год публикации должен быть не ранее 1500")
        @Max(value = 2030, message = "Год публикации должен быть не позднее 2030")
        Integer publicationYear,

        @NotBlank(message = "Описание книги обязательно")
        @Size(min = 10, max = 1000, message = "Описание должно быть от 10 до 1000 символов")
        String description,

        @NotNull(message = "Автор обязателен")
        Long authorId,

        String coverUrl,

        @Null LocalDateTime createdAt,
        @Null LocalDateTime updatedAt,

        @NotNull(message = "Список жанров не может быть null")
        Set<Long> genreIds,

        @NotNull(message = "Список тегов не может быть null")
        Set<Long> tagIds
) {
    public Book {
        if (genreIds == null) genreIds = Set.of();
        if (tagIds == null) tagIds = Set.of();
    }
}
