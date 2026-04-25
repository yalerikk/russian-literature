package com.literature.russian_literature.tags.domain;

import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagMapper;
import com.literature.russian_literature.tags.db.TagRepository;
import com.literature.russian_literature.tags.util.TagNormalizer;
import com.literature.russian_literature.tags.util.TagValidator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private static final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository repository;
    private final TagMapper mapper;
    private final TagValidator validator;
    private final TagNormalizer normalizer;
    private final BookRepository bookRepository;

    public TagService(TagRepository repository, TagMapper mapper,
                      TagValidator validator, TagNormalizer normalizer, BookRepository bookRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.bookRepository = bookRepository;
    }

    public Tag getTagById(Long id) {
        TagEntity tagEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id = " + id + " not found"));
        return mapper.toDomain(tagEntity);
    }

    public Page<TagEntity> getAllTagsForAdmin(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Tag> getTagsByType(TagType type) {
        return repository.findByType(type).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public Tag createTag(Tag tagToCreate) {
        Tag normalizedTag = normalizer.normalizeTag(tagToCreate);
        validator.validateForCreate(normalizedTag);

        var entityToSave = mapper.toEntity(normalizedTag);
        var savedEntity = repository.save(entityToSave);
        log.info("Created tag: '{}' (type: {}) with id = {}",
                savedEntity.getName(), savedEntity.getType(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Tag updateTag(Long id, Tag tag) {
        TagEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id = " + id + " not found"));

        Tag normalizedTag = normalizer.normalizeTag(tag);
        validator.validateForUpdate(id, normalizedTag);

        existing.setName(normalizedTag.name());
        existing.setType(normalizedTag.type());
        TagEntity updated = repository.save(existing);
        log.info("Updated tag: '{}' with id = {}", updated.getName(), updated.getId());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteTag(Long id) {
        TagEntity tag = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id = " + id + " not found"));

        if (bookRepository.existsByTags_Id(id)) {
            log.warn("Tag '{}' is used in books, links will be deleted", tag.getName());
            bookRepository.deleteAllTagLinks(id);
        }

        repository.deleteById(id);
        log.info("Deleted tag: '{}' with id = {}", tag.getName(), id);
    }

    public boolean tagExists(String name) {
        return repository.existsByName(name);
    }
}
