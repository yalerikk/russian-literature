package com.literature.russian_literature.catalog.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCustomCategoryDto(
        @NotBlank(message = "Название категории обязательно")
        @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
        String name,

        Boolean isActive
) {

}
