package com.literature.russian_literature.catalog.util;

import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.util.StringNormalizer;
import org.springframework.stereotype.Component;

@Component
public class CatalogCategoryNormalizer {
    private final StringNormalizer stringNormalizer;

    public CatalogCategoryNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public CatalogCategory normalize(CatalogCategory category) {
        return new CatalogCategory(
                category.id(),
                stringNormalizer.normalizeSpacesAndYo(category.name()),
                category.code().toLowerCase().trim(),
                category.displayOrder(),
                category.isActive(),
                category.booksToShow(),
                category.criteriaType().toUpperCase(),
                category.genreId(),
                category.authorId(),
                category.minPublicationYear(),
                category.maxPublicationYear(),
                category.minRating(),
                category.daysInterval(),
                category.customQuery(),
                category.createdAt(),
                category.updatedAt()
        );
    }
}
