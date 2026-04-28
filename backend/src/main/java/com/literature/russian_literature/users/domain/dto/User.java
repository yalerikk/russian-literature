package com.literature.russian_literature.users.domain.dto;

import com.literature.russian_literature.users.domain.UserRole;

import jakarta.validation.constraints.*;

public record User(
        @Null
        Long id,

        @NotBlank(message = "Логин пользователя не должен быть пустым")
        @Size(min = 2, max = 50, message = "Логин пользователя должен быть от 2 до 50 символов")
        String username,

        @NotBlank(message = "Email пользователя не должен быть пустым")
        @Email(message = "Некорректный ввод email")
        String email,

        @NotBlank(message = "Пароль пользователя не должен быть пустым")
        @Size(min = 6, message = "Пароль пользователя должен быть не менее 6 символов")
        String password,

        @NotNull(message = "Роль пользователя не может быть пустой")
        UserRole role
) {

}
