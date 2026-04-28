package com.literature.russian_literature.tags.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record Tag(
        Long id,

        @NotBlank(message = "Название тега обязательно")
        @Size(min = 1, max = 50, message = "Название тега должно быть от 1 до 50 символов")
        String name,

        @NotNull(message = "Тип тега не может быть пустым")
        TagType type
) {

}
