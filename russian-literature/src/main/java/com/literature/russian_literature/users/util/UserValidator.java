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
        validateEmail(user.email());
        validateUsernameUniqueness(user.username());
        validateEmailUniqueness(user.email());
        validatePassword(user.password());
    }

    public void validateForUpdate(Long id, User user) {
        validateRequiredFields(user);
        validateEmail(user.email());
        validateUsernameUniquenessOnUpdate(id, user.username());
        validateEmailUniquenessOnUpdate(id, user.email());

        if (user.password() != null) {
            validatePassword(user.password());
        }
    }

    private void validateRequiredFields(User user) {
        globalValidator.validateNotBlank(user.username(), "Username");
        globalValidator.validateNotBlank(user.email(), "Email");

        if (user.password() != null) {
            globalValidator.validateNotBlank(user.password(), "Password");
        }
    }

    private void validateUsernameUniqueness(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User with username '" + username + "' already exists");
        }
    }

    private void validateUsernameUniquenessOnUpdate(Long id, String username) {
        if (username != null) {
            userRepository.findByUsername(username)
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new IllegalArgumentException("User with username '" + username + "' already exists");
                        }
                    });
        }
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email '" + email + "' already exists");
        }
    }

    private void validateEmailUniquenessOnUpdate(Long id, String email) {
        if (email != null) {
            userRepository.findByEmail(email)
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new IllegalArgumentException("User with email '" + email + "' already exists");
                        }
                    });
        }
    }

    public void validateEmail(String email) {
        if (email != null && !email.isBlank()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    private void validatePassword(String password) {
        if (password == null) return;

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        if (password.matches(".*[а-яА-ЯёЁ].*")) {
            throw new IllegalArgumentException("Password must not contain Cyrillic characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
    }
}
