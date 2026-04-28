package com.literature.russian_literature.users.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Логин или email обязателен")
        String login, // может быть как username, так и email

        @NotBlank(message = "Пароль обязателен")
        String password
) {

}
