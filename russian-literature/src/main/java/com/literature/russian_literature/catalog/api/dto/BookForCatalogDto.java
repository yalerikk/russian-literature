package com.literature.russian_literature.catalog.api.dto;

import java.time.LocalDateTime;

public record BookForCatalogDto(
        Long id,
        String title,
        Integer publicationYear,
        String description,
        Long authorId,
        String authorName, // Полное имя автора
        String authorShortName, // Краткое имя (Ф.И.О.)
        String coverUrl,
        LocalDateTime createdAt,
        Double rating, // Новое поле
        Integer ratingCount // Новое поле
) {}
