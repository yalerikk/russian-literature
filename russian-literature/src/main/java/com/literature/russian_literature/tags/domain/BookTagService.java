package com.literature.russian_literature.tags.domain;

import com.literature.russian_literature.tags.db.BookTagEntity;
import com.literature.russian_literature.tags.db.BookTagMapper;
import com.literature.russian_literature.tags.db.BookTagRepository;
import com.literature.russian_literature.tags.util.BookTagNormalizer;
import com.literature.russian_literature.tags.util.BookTagValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookTagService {
    private static final Logger log = LoggerFactory.getLogger(BookTagService.class);

    private final BookTagRepository repository;
    private final BookTagMapper mapper;
    private final BookTagValidator validator;
    private final BookTagNormalizer normalizer;

    public BookTagService(BookTagRepository repository, BookTagMapper mapper,
                          BookTagValidator validator, BookTagNormalizer normalizer) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
    }

    public BookTag getBookTagById(Long id) {
        BookTagEntity tagEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тег с id = " + id + " не найден"));
        return mapper.toDomain(tagEntity);
    }

    public List<BookTag> getAllBookTags() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public BookTag getBookTagByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Тег с названием '" + name + "' не найден"));
    }

    public List<BookTag> getBookTagsByType(TagType type) {
        return repository.findByType(type).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public BookTag createBookTag(BookTag tagToCreate) {
        // Нормализация -> Валидация
        BookTag normalizedTag = normalizer.normalizeBookTag(tagToCreate);
        validator.validateForCreate(normalizedTag);

        var entityToSave = mapper.toEntity(normalizedTag);
        var savedEntity = repository.save(entityToSave);
        log.info("Создан тег: '{}' (тип: {}) с id = {}",
                savedEntity.getName(), savedEntity.getType(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public BookTag updateBookTag(Long id, BookTag tag) {
        BookTagEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тег с id = " + id + " не найден"));

        // Нормализация -> Валидация
        BookTag normalizedTag = normalizer.normalizeBookTag(tag);
        validator.validateForUpdate(id, normalizedTag);

        existing.setName(normalizedTag.name());
        existing.setType(normalizedTag.type());
        BookTagEntity updated = repository.save(existing);
        log.info("Обновлен тег: '{}' с id = {}", updated.getName(), updated.getId());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteBookTag(Long id) {
        BookTagEntity tag = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тег с id = " + id + " не найден"));
        repository.deleteById(id);
        log.info("Удален тег: '{}' с id = {}", tag.getName(), id);
    }

    public boolean bookTagExists(String name) {
        return repository.existsByName(name);
    }
}