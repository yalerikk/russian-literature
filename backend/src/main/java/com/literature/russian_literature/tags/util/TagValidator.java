package com.literature.russian_literature.tags.util;

import com.literature.russian_literature.tags.db.TagRepository;
import com.literature.russian_literature.tags.domain.Tag;
import com.literature.russian_literature.util.GlobalValidator;

import org.springframework.stereotype.Component;

@Component
public class TagValidator {
    private final TagRepository repository;
    private final GlobalValidator globalValidator;

    public TagValidator(TagRepository repository, GlobalValidator globalValidator) {
        this.repository = repository;
        this.globalValidator = globalValidator;
    }

    public void validateForCreate(Tag tag) {
        validateRequiredFields(tag);
        validateNameUniqueness(tag.name());
    }

    public void validateForUpdate(Long id, Tag tag) {
        validateRequiredFields(tag);
        validateNameUniquenessOnUpdate(id, tag.name());
    }

    private void validateRequiredFields(Tag tag) {
        globalValidator.validateNotBlank(tag.name(), "Tag name");

        if (tag.name().length() > 50) {
            throw new IllegalArgumentException("Tag name must not exceed 50 characters");
        }

        if (tag.type() == null) {
            throw new IllegalArgumentException("Tag type is required");
        }
    }

    private void validateNameUniqueness(String name) {
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Tag with name '" + name + "' already exists");
        }
    }

    private void validateNameUniquenessOnUpdate(Long id, String name) {
        repository.findByName(name)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Tag with name '" + name + "' already exists");
                    }
                });
    }
}
