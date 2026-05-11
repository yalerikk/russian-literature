package com.literature.russian_literature.catalog.domain.dto;

public record BookForCatalogDto(
        Long id,
        String title,
        String coverUrl,
        String authorShortName, // Краткое имя (Ф.И.О.)
        Double rating, // Новое поле
        Integer ratingCount, // Новое поле
        boolean favorite
) {

}
