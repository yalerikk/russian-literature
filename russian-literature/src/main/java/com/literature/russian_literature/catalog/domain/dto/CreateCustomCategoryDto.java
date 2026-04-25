package com.literature.russian_literature.catalog.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateCustomCategoryDto(
        @NotNull(message = "Список тегов обязателен")
        Set<Long> tagIds
) {

}
