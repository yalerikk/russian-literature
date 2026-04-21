package com.literature.russian_literature.catalog.domain;

import com.literature.russian_literature.catalog.db.CatalogCategoryEntity;
import com.literature.russian_literature.catalog.db.CatalogCategoryMapper;
import com.literature.russian_literature.catalog.db.CatalogCategoryRepository;
import com.literature.russian_literature.catalog.util.CatalogCategoryValidator;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagRepository;
import com.literature.russian_literature.tags.domain.TagType;
import com.literature.russian_literature.util.StringNormalizer;
import com.literature.russian_literature.util.TranslitUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CatalogCategoryService {
    private static final Logger log = LoggerFactory.getLogger(CatalogCategoryService.class);

    private final CatalogCategoryRepository repository;
    private final CatalogCategoryMapper mapper;
    private final CatalogCategoryValidator validator;
    private final TagRepository tagRepository;
    private final StringNormalizer normalizer;

    @Autowired
    public CatalogCategoryService(
            CatalogCategoryRepository repository, CatalogCategoryMapper mapper,
            CatalogCategoryValidator validator, TagRepository tagRepository, StringNormalizer normalizer) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.tagRepository = tagRepository;
        this.normalizer = normalizer;
    }

    public List<CatalogCategory> getAllCategories() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<CatalogCategory> getCategoriesFiltered(Boolean isActive) {
        if (isActive == null) return getAllCategories();
        return repository.findByIsActive(isActive).stream().map(mapper::toDomain).toList();
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

    public List<CatalogCategory> getActiveCategories() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public CatalogCategory createCustomCategory(Set<Long> tagIds) {
        validator.validateCreate(tagIds);

        if (tagRepository.countExistingTagsByIds(tagIds) != tagIds.size()) {
            throw new IllegalArgumentException("Некоторые теги не найдены, проверьте корректность вводимых данных");
        }

        Set<TagEntity> tags = new HashSet<>(tagRepository.findAllById(tagIds));
        if (tags.isEmpty()) throw new IllegalArgumentException("Теги не найдены");

        // Проверка, что нет двух тегов одного типа
        Set<TagType> types = tags.stream().map(TagEntity::getType).collect(Collectors.toSet());
        if (types.size() != tags.size()) {
            throw new IllegalArgumentException("В категории не может быть двух тегов одного типа (например, два класса или два уровня)");
        }

        String name = generateNameFromTags(tags);
        String code = generateCodeFromTags(tags);

        validator.validateNameAndCodeInCreate(code, name);

        Integer maxOrder = repository.findMaxDisplayOrder();
        int displayOrder = (maxOrder == null ? 0 : maxOrder + 1);

        CatalogCategoryEntity entity = new CatalogCategoryEntity();
        entity.setName(name);
        entity.setCode(code);
        entity.setDisplayOrder(displayOrder);
        entity.setIsActive(true);
        entity.setBooksToShow(7);
        entity.setCriteriaType("CUSTOM");
        entity.setTags(tags);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CatalogCategoryEntity saved = repository.save(entity);
        log.info("Создана кастомная категория: {}", saved.getName());
        return mapper.toDomain(saved);
    }

    @Transactional
    public CatalogCategory updateCategory(Long id, String name, Boolean isActive) {
        CatalogCategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория каталога с id = " + id + " не найдена"
                ));

        if (existing.getCriteriaType().equals("NEW") || existing.getCriteriaType().equals("POPULAR") || existing.getCriteriaType().equals("BY_PERIOD")) {
            throw new IllegalArgumentException("Базовые категории нельзя редактировать");
        }

        String normalizedName = normalizer.normalizeName(name);
        validator.validateUpdate(id, normalizedName, isActive);

        if (normalizedName != null) existing.setName(normalizedName);
        if (isActive != null) existing.setIsActive(isActive);
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

        if (category.getCriteriaType().equals("NEW") || category.getCriteriaType().equals("POPULAR") || category.getCriteriaType().equals("BY_PERIOD")) {
            throw new IllegalArgumentException("Нельзя удалить базовую категорию");
        }

        repository.deleteById(id);
        log.info("Удалена категория каталога: '{}' (id: {})", category.getName(), id);
    }

    private String generateNameFromTags(Set<TagEntity> tags) {
        // Сортировка по типу тега: GRADE, LEVEL, CATEGORY, READING_TYPE
        String raw = tags.stream()
                .sorted(Comparator.comparingInt(t -> t.getType().ordinal()))
                .map(t -> {
                    String name = t.getName();
                    if (Character.isDigit(name.charAt(0))) return name;
                    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                })
                .collect(Collectors.joining(", "));
        if (raw.isEmpty()) return "";
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1).toLowerCase();
    }

    private String generateCodeFromTags(Set<TagEntity> tags) {
        return tags.stream()
                .sorted(Comparator.comparingInt(t -> t.getType().ordinal()))
                .map(t -> TranslitUtil.translit(t.getName()))
                .collect(Collectors.joining("_"));
    }
}
