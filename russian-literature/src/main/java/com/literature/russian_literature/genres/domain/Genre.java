package com.literature.russian_literature.genres.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Genre(
        Long id,

        @NotBlank(message = "Название жанра обязательно")
        @Size(min = 1, max = 50, message = "Название жанра должно быть от 1 до 50 символов")
        String name
) {

}
