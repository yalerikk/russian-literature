package com.literature.russian_literature.catalog.domain;

import com.literature.russian_literature.catalog.db.CatalogCategoryEntity;
import com.literature.russian_literature.catalog.db.CatalogCategoryMapper;
import com.literature.russian_literature.catalog.db.CatalogCategoryRepository;
import com.literature.russian_literature.catalog.util.CatalogCategoryValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CatalogCategoryService {
    private static final Logger log = LoggerFactory.getLogger(CatalogCategoryService.class);

    private final CatalogCategoryRepository repository;
    private final CatalogCategoryMapper mapper;
    private final CatalogCategoryValidator validator;

    @Autowired
    public CatalogCategoryService(
            CatalogCategoryRepository repository,
            CatalogCategoryMapper mapper,
            CatalogCategoryValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    public CatalogCategory getCategoryById(Long id) {
        CatalogCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория каталога с id = " + id + " не найдена"
                ));
        return mapper.toDomain(entity);
    }

    public CatalogCategory getCategoryByCode(String code) {
        CatalogCategoryEntity entity = repository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория каталога с кодом = " + code + " не найдена"
                ));
        return mapper.toDomain(entity);
    }

    public List<CatalogCategory> getAllCategories() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<CatalogCategory> getActiveCategories() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public CatalogCategory createCategory(CatalogCategory category) {
        validator.validateCreate(category);

        // Если порядок не указан, ставим в конец
        CatalogCategory categoryToSave = category;
        if (category.displayOrder() == null) {
            Integer maxOrder = repository.findMaxDisplayOrder();
            int newOrder = maxOrder != null ? maxOrder + 1 : 0;
            categoryToSave = new CatalogCategory(
                    category.id(),
                    category.name(),
                    category.code(),
                    newOrder,
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
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }

        CatalogCategoryEntity entity = mapper.toEntity(categoryToSave);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CatalogCategoryEntity saved = repository.save(entity);
        log.info("Создана категория каталога: '{}' (код: {})", saved.getName(), saved.getCode());
        return mapper.toDomain(saved);
    }

    @Transactional
    public CatalogCategory updateCategory(Long id, CatalogCategory category) {
        CatalogCategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория каталога с id = " + id + " не найдена"
                ));

        validator.validateUpdate(id, category);

        // Обновляем поля
        existing.setName(category.name());
        existing.setCode(category.code());
        existing.setDisplayOrder(category.displayOrder());
        existing.setIsActive(category.isActive());
        existing.setBooksToShow(category.booksToShow());
        existing.setCriteriaType(category.criteriaType());
        existing.setGenreId(category.genreId());
        existing.setAuthorId(category.authorId());
        existing.setMinPublicationYear(category.minPublicationYear());
        existing.setMaxPublicationYear(category.maxPublicationYear());
        existing.setMinRating(category.minRating());
        existing.setDaysInterval(category.daysInterval());
        existing.setCustomQuery(category.customQuery());
        existing.setUpdatedAt(LocalDateTime.now());

        CatalogCategoryEntity updated = repository.save(existing);
        log.info("Обновлена категория каталога: '{}' (id: {})", updated.getName(), updated.getId());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteCategory(Long id) {
        CatalogCategoryEntity category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория каталога с id = " + id + " не найдена"
                ));

        repository.deleteById(id);
        log.info("Удалена категория каталога: '{}' (id: {})", category.getName(), id);
    }

    @Transactional
    public void reorderCategories(List<Long> categoryIdsInOrder) {
        for (int i = 0; i < categoryIdsInOrder.size(); i++) {
            Long categoryId = categoryIdsInOrder.get(i);
            CatalogCategoryEntity category = repository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Категория каталога с id = " + categoryId + " не найдена"
                    ));
            category.setDisplayOrder(i);
            category.setUpdatedAt(LocalDateTime.now());
            repository.save(category);
        }
        log.info("Изменен порядок категорий каталога");
    }
}
