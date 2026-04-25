package com.literature.russian_literature.catalog.util;

import com.literature.russian_literature.catalog.db.CatalogCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CatalogCategoryValidator {
    private final CatalogCategoryRepository repository;

    @Autowired
    public CatalogCategoryValidator(CatalogCategoryRepository repository) {
        this.repository = repository;
    }

    public void validateCreate(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            throw new IllegalArgumentException("A custom category must have at least one tag");
        }
    }

    public void validateUpdate(Long id, String newName, Boolean isActive) {
        if (newName != null && (newName.length() < 2 || newName.length() > 100)) {
            throw new IllegalArgumentException("Category name must be between 2 and 100 characters");
        }
        if (newName != null && repository.existsByNameAndIdNot(newName, id)) {
            throw new IllegalArgumentException("Category with name '" + newName + "' already exists");
        }
    }

    public void validateNameAndCodeInCreate(String code, String name) {
        if (repository.existsByCode(code)) {
            throw new IllegalArgumentException("Category with code '" + code + "' already exists");
        }
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Category with name '" + name + "' already exists");
        }
    }
}
