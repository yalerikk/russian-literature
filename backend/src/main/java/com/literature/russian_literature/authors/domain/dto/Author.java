package com.literature.russian_literature.authors.domain.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record Author(
        @Null Long id,

        @NotBlank(message = "Имя автора обязательно")
        @Size(min = 1, max = 50, message = "Имя должно быть от 1 до 50 символов")
        String firstName,

        @NotBlank(message = "Фамилия автора обязательна")
        @Size(min = 1, max = 50, message = "Фамилия должна быть от 1 до 50 символов")
        String lastName,

        @NotBlank(message = "Отчество автора обязательно")
        @Size(min = 1, max = 50, message = "Отчество должно быть от 1 до 50 символов")
        String middleName,

        @NotNull(message = "Дата рождения обязательна")
        LocalDate birthDate,

        LocalDate deathDate,

        @NotBlank(message = "Биография обязательна")
        @Size(min = 10, max = 2000, message = "Биография должна быть от 10 до 2000 символов")
        String biography,

        String photoUrl,
        Integer bookCount
) {

}
