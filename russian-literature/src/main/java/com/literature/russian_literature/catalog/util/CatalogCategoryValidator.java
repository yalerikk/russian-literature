package com.literature.russian_literature.catalog.util;

import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.catalog.db.CatalogCategoryRepository;
import com.literature.russian_literature.util.GlobalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class CatalogCategoryValidator {

    private final CatalogCategoryRepository repository;
    private final GlobalValidator globalValidator;

    @Autowired
    public CatalogCategoryValidator(
            CatalogCategoryRepository repository,
            GlobalValidator globalValidator) {
        this.repository = repository;
        this.globalValidator = globalValidator;
    }

    public void validateCreate(CatalogCategory category) {
        validateRequiredFields(category);
        validateCodeUniqueness(category.code());
        validateCriteria(category);
    }

    public void validateUpdate(Long id, CatalogCategory category) {
        validateRequiredFields(category);
        validateCodeUniquenessOnUpdate(id, category.code());
        validateCriteria(category);
    }

    private void validateRequiredFields(CatalogCategory category) {
        globalValidator.validateNotBlank(category.name(), "Название категории");
        globalValidator.validateNotBlank(category.code(), "Код категории");

        if (category.name().length() < 2 || category.name().length() > 100) {
            throw new IllegalArgumentException("Название категории должно быть от 2 до 100 символов");
        }

        if (!category.code().matches("^[a-z_]+$")) {
            throw new IllegalArgumentException("Код категории должен содержать только строчные латинские буквы и подчеркивания");
        }

        if (category.booksToShow() < 1 || category.booksToShow() > 20) {
            throw new IllegalArgumentException("Количество книг для отображения должно быть от 1 до 20");
        }
    }

    private void validateCodeUniqueness(String code) {
        if (repository.existsByCode(code)) {
            throw new IllegalArgumentException("Категория с кодом '" + code + "' уже существует");
        }
    }

    private void validateCodeUniquenessOnUpdate(Long id, String code) {
        if (repository.existsByCodeAndIdNot(code, id)) {
            throw new IllegalArgumentException("Категория с кодом '" + code + "' уже существует");
        }
    }

    private void validateCriteria(CatalogCategory category) {
        try {
            CatalogCategory.CriteriaType.valueOf(category.criteriaType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный тип критерия: " + category.criteriaType());
        }

        switch (CatalogCategory.CriteriaType.valueOf(category.criteriaType())) {
            case BY_GENRE:
                if (category.genreId() == null) {
                    throw new IllegalArgumentException("Для категории по жанру необходимо указать genreId");
                }
                break;
            case BY_AUTHOR:
                if (category.authorId() == null) {
                    throw new IllegalArgumentException("Для категории по автору необходимо указать authorId");
                }
                break;
            case BY_PERIOD:
                if (category.minPublicationYear() == null && category.maxPublicationYear() == null) {
                    throw new IllegalArgumentException("Для категории по периоду необходимо указать хотя бы один год");
                }
                if (category.minPublicationYear() != null && category.maxPublicationYear() != null
                        && category.minPublicationYear() > category.maxPublicationYear()) {
                    throw new IllegalArgumentException("Минимальный год не может быть больше максимального");
                }
                break;
            case NEW:
                if (category.daysInterval() == null || category.daysInterval() <= 0) {
                    throw new IllegalArgumentException("Для категории 'Новинки' необходимо указать положительный интервал дней");
                }
                break;
            case CUSTOM:
                if (category.customQuery() == null || category.customQuery().trim().isEmpty()) {
                    throw new IllegalArgumentException("Для кастомной категории необходимо указать запрос");
                }
                break;
        }
    }
}
