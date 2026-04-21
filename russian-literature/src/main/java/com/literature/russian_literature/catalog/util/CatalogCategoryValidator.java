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
            throw new IllegalArgumentException("Для кастомной категории нужно указать хотя бы один тег");
        }
    }

    public void validateUpdate(Long id, String newName, Boolean isActive) {
        if (newName != null && (newName.length() < 2 || newName.length() > 100)) {
            throw new IllegalArgumentException("Название категории должно быть от 2 до 100 символов");
        }
        if (newName != null && repository.existsByNameAndIdNot(newName, id)) {
            throw new IllegalArgumentException("Категория с названием '" + newName + "' уже существует");
        }
    }

    public void validateNameAndCodeInCreate(String code, String name) {
        if (repository.existsByCode(code)) {
            throw new IllegalArgumentException("Категория с кодом '" + code + "' уже существует");
        }
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Категория с названием '" + name + "' уже существует");
        }
    }
}
