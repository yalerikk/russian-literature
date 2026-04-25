package com.literature.russian_literature.genres.util;

import com.literature.russian_literature.genres.domain.Genre;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.util.GlobalValidator;

import org.springframework.stereotype.Component;

@Component
public class GenreValidator {
    private final GenreRepository repository;
    private final GlobalValidator globalValidator;

    public GenreValidator(GenreRepository repository, GlobalValidator globalValidator) {
        this.repository = repository;
        this.globalValidator = globalValidator;
    }

    public void validateForCreate(Genre genre) {
        validateRequiredFields(genre);
        validateNameUniqueness(genre.name());
    }

    public void validateForUpdate(Long id, Genre genre) {
        validateRequiredFields(genre);
        validateNameUniquenessOnUpdate(id, genre.name());
    }

    private void validateRequiredFields(Genre genre) {
        globalValidator.validateNotBlank(genre.name(), "Genre name");

        if (genre.name().length() > 50) {
            throw new IllegalArgumentException("Genre name must not exceed 50 characters");
        }
    }

    private void validateNameUniqueness(String name) {
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Genre with name '" + name + "' already exists");
        }
    }

    private void validateNameUniquenessOnUpdate(Long id, String name) {
        repository.findByName(name)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Genre with name '" + name + "' already exists");
                    }
                });
    }
}
