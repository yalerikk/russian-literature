package com.literature.russian_literature.authors.util;

import com.literature.russian_literature.authors.domain.Author;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.util.GlobalValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuthorValidator {

    private final AuthorRepository repository;
    private final GlobalValidator globalValidator;

    @Autowired
    public AuthorValidator(AuthorRepository repository, GlobalValidator globalValidator) {
        this.repository = repository;
        this.globalValidator = globalValidator;
    }

    public void validateCreate(Author author) {
        validateRequiredFields(author);
        validateFullNameUniqueness(author.firstName(), author.lastName(), author.middleName());
        validateDates(author.birthDate(), author.deathDate());
        validateBiographyLength(author.biography());
        globalValidator.validatePhotoUrl(author.photoUrl());
    }

    public void validateUpdate(Long id, Author author) {
        validateRequiredFields(author);
        validateFullNameUniquenessOnUpdate(id, author.firstName(), author.lastName(), author.middleName());
        validateDates(author.birthDate(), author.deathDate());
        validateBiographyLength(author.biography());
        globalValidator.validatePhotoUrl(author.photoUrl());
    }

    private void validateRequiredFields(Author author) {
        globalValidator.validateNotBlank(author.firstName(), "Имя");
        globalValidator.validateNotBlank(author.lastName(), "Фамилия");
        globalValidator.validateNotBlank(author.middleName(), "Отчество");
        globalValidator.validateNotBlank(author.biography(), "Биография");
        globalValidator.validateNotBlank(author.photoUrl(), "URL фотографии");

        // Дополнительная проверка длины после нормализации
        if (author.firstName().length() > 50) {
            throw new IllegalArgumentException("Имя не должно превышать 50 символов");
        }
        if (author.lastName().length() > 50) {
            throw new IllegalArgumentException("Фамилия не должна превышать 50 символов");
        }
        if (author.middleName().length() > 50) {
            throw new IllegalArgumentException("Отчество не должно превышать 50 символов");
        }
    }

    private void validateFullNameUniqueness(String firstName, String lastName, String middleName) {
        if (repository.existsByFullName(firstName, lastName, middleName)) {
            throw new IllegalArgumentException("Автор с таким ФИО уже существует");
        }
    }

    private void validateFullNameUniquenessOnUpdate(Long id, String firstName, String lastName, String middleName) {
        if (repository.existsByFullNameExcludingId(firstName, lastName, middleName, id)) {
            throw new IllegalArgumentException("Автор с таким ФИО уже существует");
        }
    }

    private void validateBiographyLength(String biography) {
        if (biography.length() < 10) {
            throw new IllegalArgumentException("Биография должна содержать не менее 10 символов");
        }
        if (biography.length() > 2000) {
            throw new IllegalArgumentException("Биография не должна превышать 2000 символов");
        }
    }

    private void validateDates(LocalDate birthDate, LocalDate deathDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Дата рождения обязательна");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем");
        }
        if (deathDate != null) {
            if (deathDate.isBefore(birthDate)) {
                throw new IllegalArgumentException("Дата смерти не может быть раньше даты рождения");
            }
            if (deathDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Дата смерти не может быть в будущем");
            }
            if (deathDate.isEqual(birthDate)) {
                throw new IllegalArgumentException("Дата смерти не может быть равна дате рождения");
            }
            // Проверка на минимальный и максимальный возраст
            if (deathDate.isBefore(birthDate.plusYears(10)) || deathDate.isAfter(birthDate.plusYears(150))) {
                throw new IllegalArgumentException("Дата смерти нереалистична - автор не мог умереть в таком возрасте");
            }
        }
    }
}
