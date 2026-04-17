package com.literature.russian_literature.users.util;

import com.literature.russian_literature.users.db.UserRepository;
import com.literature.russian_literature.users.domain.dto.User;
import com.literature.russian_literature.util.GlobalValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private final UserRepository userRepository;
    private final GlobalValidator globalValidator;

    public UserValidator(UserRepository userRepository, GlobalValidator globalValidator) {
        this.userRepository = userRepository;
        this.globalValidator = globalValidator;
    }

    public void validateForCreate(User user) {
        validateRequiredFields(user);
        validateEmail(user.email()); // Валидация email
        validateUsernameUniqueness(user.username());
        validateEmailUniqueness(user.email());
        validatePassword(user.password());
    }

    public void validateForUpdate(Long id, User user) {
        validateRequiredFields(user);
        validateEmail(user.email()); // Валидация email
        validateUsernameUniquenessOnUpdate(id, user.username());
        validateEmailUniquenessOnUpdate(id, user.email());

        // Пароль при обновлении может быть null (не обновляется)
        if (user.password() != null) {
            validatePassword(user.password());
        }
    }

    private void validateRequiredFields(User user) {
        globalValidator.validateNotBlank(user.username(), "Логин");
        globalValidator.validateNotBlank(user.email(), "Email");

        // Для создания пароль обязателен, для обновления - нет
        if (user.password() != null) {
            globalValidator.validateNotBlank(user.password(), "Пароль");
        }
    }

    private void validateUsernameUniqueness(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с логином '" + username + "' уже существует");
        }
    }

    private void validateUsernameUniquenessOnUpdate(Long id, String username) {
        if (username != null) {
            userRepository.findByUsername(username)
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new IllegalArgumentException("Пользователь с логином '" + username + "' уже существует");
                        }
                    });
        }
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с email '" + email + "' уже существует");
        }
    }

    private void validateEmailUniquenessOnUpdate(Long id, String email) {
        if (email != null) {
            userRepository.findByEmail(email)
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new IllegalArgumentException("Пользователь с email '" + email + "' уже существует");
                        }
                    });
        }
    }

    /**
     * Проверяет формат email
     */
    public void validateEmail(String email) {
        if (email != null && !email.isBlank()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Некорректный формат email");
            }
        }
    }

    /**
     * Проверяет формат пароля
     */
    private void validatePassword(String password) {
        if (password == null) return;

        if (password.length() < 6) {
            throw new IllegalArgumentException("Пароль должен быть не менее 6 символов");
        }

        // Проверка на кириллицу
        if (password.matches(".*[а-яА-ЯёЁ].*")) {
            throw new IllegalArgumentException("Пароль не должен содержать кириллицу");
        }

        // Дополнительные проверки сложности (по желанию)
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Пароль должен содержать хотя бы одну заглавную латинскую букву");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Пароль должен содержать хотя бы одну строчную латинскую букву");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Пароль должен содержать хотя бы одну цифру");
        }
    }
}