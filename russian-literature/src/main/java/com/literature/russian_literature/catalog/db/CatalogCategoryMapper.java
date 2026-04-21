package com.literature.russian_literature.catalog.db;

import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.tags.db.TagEntity;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CatalogCategoryMapper {
    public CatalogCategory toDomain(CatalogCategoryEntity entity) {
        Set<Long> tagIds = entity.getTags().stream()
                .map(TagEntity::getId)
                .collect(Collectors.toSet());

        return new CatalogCategory(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getDisplayOrder(),
                entity.getIsActive(),
                entity.getBooksToShow(),
                entity.getCriteriaType(),
                entity.getMinPublicationYear(),
                entity.getMaxPublicationYear(),
                entity.getMinRating(),
                entity.getDaysInterval(),
                tagIds,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public CatalogCategoryEntity toEntity(CatalogCategory category, Set<TagEntity> tags) {
        var entity = new CatalogCategoryEntity();

        entity.setId(category.id());
        entity.setName(category.name());
        entity.setCode(category.code());
        entity.setDisplayOrder(category.displayOrder());
        entity.setIsActive(category.isActive());
        entity.setBooksToShow(category.booksToShow());
        entity.setCriteriaType(category.criteriaType());
        entity.setMinPublicationYear(category.minPublicationYear());
        entity.setMaxPublicationYear(category.maxPublicationYear());
        entity.setMinRating(category.minRating());
        entity.setDaysInterval(category.daysInterval());
        entity.setTags(tags);
        entity.setCreatedAt(category.createdAt());
        entity.setUpdatedAt(category.updatedAt());

        return entity;
    }
}
