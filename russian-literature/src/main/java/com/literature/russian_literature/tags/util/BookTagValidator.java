package com.literature.russian_literature.tags.util;

import com.literature.russian_literature.tags.db.BookTagRepository;
import com.literature.russian_literature.tags.domain.BookTag;
import com.literature.russian_literature.util.GlobalValidator;
import org.springframework.stereotype.Component;

@Component
public class BookTagValidator {
    private final BookTagRepository repository;
    private final GlobalValidator globalValidator;

    public BookTagValidator(BookTagRepository repository, GlobalValidator globalValidator) {
        this.repository = repository;
        this.globalValidator = globalValidator;
    }

    public void validateForCreate(BookTag tag) {
        validateRequiredFields(tag);
        validateNameUniqueness(tag.name());
    }

    public void validateForUpdate(Long id, BookTag tag) {
        validateRequiredFields(tag);
        validateNameUniquenessOnUpdate(id, tag.name());
    }

    private void validateRequiredFields(BookTag tag) {
        globalValidator.validateNotBlank(tag.name(), "Название тега");

        if (tag.name().length() > 50) {
            throw new IllegalArgumentException("Название тега не должно превышать 50 символов");
        }

        if (tag.type() == null) {
            throw new IllegalArgumentException("Тип тега обязателен");
        }
    }

    private void validateNameUniqueness(String name) {
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Тег с названием '" + name + "' уже существует");
        }
    }

    private void validateNameUniquenessOnUpdate(Long id, String name) {
        repository.findByName(name)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Тег с названием '" + name + "' уже существует");
                    }
                });
    }
}