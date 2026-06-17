package com.literature.russian_literature.authors.util;

import com.literature.russian_literature.authors.domain.dto.Author;
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
    }

    public void validateUpdate(Long id, Author author) {
        validateRequiredFields(author);
        validateFullNameUniquenessOnUpdate(id, author.firstName(), author.lastName(), author.middleName());
        validateDates(author.birthDate(), author.deathDate());
        validateBiographyLength(author.biography());
        globalValidator.validatePhotoUrl(author.photoUrl());
    }

    private void validateRequiredFields(Author author) {
        globalValidator.validateNotBlank(author.firstName(), "First name");
        globalValidator.validateNotBlank(author.lastName(), "Last name");
        globalValidator.validateNotBlank(author.middleName(), "Middle name");
        globalValidator.validateNotBlank(author.biography(), "Biography");

        if (author.firstName().length() > 50) {
            throw new IllegalArgumentException("First name must not exceed 50 characters");
        }
        if (author.lastName().length() > 50) {
            throw new IllegalArgumentException("Last name must not exceed 50 characters");
        }
        if (author.middleName().length() > 50) {
            throw new IllegalArgumentException("Middle name must not exceed 50 characters");
        }
    }

    private void validateFullNameUniqueness(String firstName, String lastName, String middleName) {
        if (repository.existsByFullName(firstName, lastName, middleName)) {
            throw new IllegalArgumentException("An author with the same full name already exists");
        }
    }

    private void validateFullNameUniquenessOnUpdate(Long id, String firstName, String lastName, String middleName) {
        if (repository.existsByFullNameExcludingId(firstName, lastName, middleName, id)) {
            throw new IllegalArgumentException("An author with the same full name already exists");
        }
    }

    private void validateBiographyLength(String biography) {
        if (biography.length() < 10) {
            throw new IllegalArgumentException("Biography must contain at least 10 characters");
        }
        if (biography.length() > 2000) {
            throw new IllegalArgumentException("Biography must not exceed 2000 characters");
        }
    }

    private void validateDates(LocalDate birthDate, LocalDate deathDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        if (birthDate.isAfter(LocalDate.now()) || birthDate.isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date must be in the past (not today or future)");
        }
        if (deathDate != null) {
            if (deathDate.isBefore(birthDate)) {
                throw new IllegalArgumentException("Death date cannot be earlier than the birth date");
            }
            if (deathDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Death date cannot be in the future");
            }
            if (deathDate.isEqual(birthDate)) {
                throw new IllegalArgumentException("Death date cannot be the same as the birth date");
            }
            if (deathDate.isBefore(birthDate.plusYears(10)) || deathDate.isAfter(birthDate.plusYears(150))) {
                throw new IllegalArgumentException("Death date is unrealistic — the author could not die at this age");
            }
        }
    }
}
