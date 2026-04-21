package com.literature.russian_literature.catalog.domain;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

public record CatalogCategory(
        @Null Long id,

        @NotBlank(message = "Название категории обязательно")
        @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
        String name,

        @NotBlank(message = "Код категории обязателен")
        @Pattern(regexp = "^[a-z0-9_]+$", message = "Код должен содержать только строчные латинские буквы, цифры и подчеркивания")
        String code,

        Integer displayOrder,
        Boolean isActive,
        Integer booksToShow,

        @NotBlank(message = "Тип критерия обязателен")
        String criteriaType,

        Integer minPublicationYear,
        Integer maxPublicationYear,
        Double minRating,
        Integer daysInterval,

        Set<Long> tagIds,

        @Null LocalDateTime createdAt,
        @Null LocalDateTime updatedAt
) {
    public enum CriteriaType {
        NEW,           // Новинки (по дате добавления)
        POPULAR,       // Популярные (по рейтингу/просмотрам)
        BY_PERIOD,     // По периоду
        CUSTOM         // Кастомная выборка
    }
}
