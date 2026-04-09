package com.literature.russian_literature.catalog.domain;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CatalogCategory(
        @Null Long id,

        @NotBlank(message = "Название категории обязательно")
        @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
        String name,

        @NotBlank(message = "Код категории обязателен")
        @Pattern(regexp = "^[a-z_]+$", message = "Код должен содержать только строчные латинские буквы и подчеркивания")
        String code,

        @NotNull(message = "Порядок отображения обязателен")
        @Min(value = 0, message = "Порядок отображения не может быть отрицательным")
        Integer displayOrder,

        @NotNull(message = "Статус активности обязателен")
        Boolean isActive,

        @NotNull(message = "Количество книг для отображения обязательно")
        @Min(value = 1, message = "Должна отображаться хотя бы 1 книга")
        @Max(value = 20, message = "Нельзя отображать более 20 книг")
        Integer booksToShow,

        @NotBlank(message = "Тип критерия обязателен")
        String criteriaType,

        Long genreId,
        Long authorId,
        Integer minPublicationYear,
        Integer maxPublicationYear,
        Double minRating,
        Integer daysInterval,
        String customQuery,

        @Null LocalDateTime createdAt,
        @Null LocalDateTime updatedAt
) {
    public enum CriteriaType {
        NEW,           // Новинки (по дате добавления)
        POPULAR,       // Популярные (по рейтингу/просмотрам)
        BY_GENRE,      // По жанру
        BY_AUTHOR,     // По автору
        BY_PERIOD,     // По периоду
        CUSTOM         // Кастомная выборка
    }
}
