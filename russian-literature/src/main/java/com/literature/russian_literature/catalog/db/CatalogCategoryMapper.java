package com.literature.russian_literature.catalog.db;

import com.literature.russian_literature.catalog.domain.CatalogCategory;
import org.springframework.stereotype.Component;

@Component
public class CatalogCategoryMapper {
    public CatalogCategory toDomain(CatalogCategoryEntity entity) {
        return new CatalogCategory(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getDisplayOrder(),
                entity.getIsActive(),
                entity.getBooksToShow(),
                entity.getCriteriaType(),
                entity.getGenreId(),
                entity.getAuthorId(),
                entity.getMinPublicationYear(),
                entity.getMaxPublicationYear(),
                entity.getMinRating(),
                entity.getDaysInterval(),
                entity.getCustomQuery(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public CatalogCategoryEntity toEntity(CatalogCategory category) {
        return new CatalogCategoryEntity(
                category.id(),
                category.name(),
                category.code(),
                category.displayOrder(),
                category.isActive(),
                category.booksToShow(),
                category.criteriaType(),
                category.genreId(),
                category.authorId(),
                category.minPublicationYear(),
                category.maxPublicationYear(),
                category.minRating(),
                category.daysInterval(),
                category.customQuery(),
                category.createdAt() != null ? category.createdAt() : null,
                category.updatedAt() != null ? category.updatedAt() : null
        );
    }
}
