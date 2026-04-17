package com.literature.russian_literature.authors.domain;

import com.literature.russian_literature.authors.db.AuthorMapper;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.domain.dto.Author;
import com.literature.russian_literature.authors.domain.dto.AuthorForSelect;
import com.literature.russian_literature.authors.util.AuthorNormalizer;
import com.literature.russian_literature.authors.util.AuthorValidator;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.cloudinary.CloudinaryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository repository;
    private final AuthorMapper mapper;
    private final AuthorValidator validator;
    private final AuthorNormalizer normalizer;
    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;
    //private final BookService bookService;

    @Autowired
    public AuthorService(AuthorRepository repository, AuthorMapper mapper,
                         AuthorValidator validator, AuthorNormalizer normalizer,
                         BookRepository bookRepository, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.bookRepository = bookRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Author getAuthorById (
            Long id
    ) {
        AuthorEntity authorEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Автор с id = " + id + " не найден"
                ));

        return mapper.toDomain(authorEntity);
    }

    public List<Author> getAllAuthors() {
        List<AuthorEntity> allAuthors = repository.findAll();

        return allAuthors
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public Author createAuthor(
            Author authorToCreate
    ) {
        // Нормализация –> Валидация
        Author normalizedAuthor = normalizer.normalizeAuthor(authorToCreate);
        validator.validateCreate(normalizedAuthor);

        var entityToSave = mapper.toEntity(normalizedAuthor);
        var savedEntity = repository.save(entityToSave);
        log.info("Создан автор: '{}' с id = {}", savedEntity.getFullName(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Author updateAuthor(Long id, Author author) {
        AuthorEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автор с id = " + id + " не найден"));

        validator.validateUpdate(id, author);
        Author normalizedAuthor = normalizer.normalizeAuthor(author);

        // Обновляем поля
        existing.setFirstName(normalizedAuthor.firstName());
        existing.setLastName(normalizedAuthor.lastName());
        existing.setMiddleName(normalizedAuthor.middleName());
        existing.setBirthDate(normalizedAuthor.birthDate());
        existing.setDeathDate(normalizedAuthor.deathDate());
        existing.setBiography(normalizedAuthor.biography());
        existing.setPhotoUrl(normalizedAuthor.photoUrl());

        AuthorEntity updated = repository.save(existing);
        log.info("Обновлен автор: '{}' с id = {}", updated.getFullName(), updated.getId());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        var author = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автор с id = " + id + " не найден"));

        if (bookRepository.existsByAuthorId(id)) {
            throw new IllegalStateException("Невозможно удалить автора '" + author.getFullName() +
                    "', так как у него есть книги. Сначала удалите или переназначьте книги.");
        }

        // Удаляем фото из Cloudinary, если оно есть
        try {
            if (author.getPhotoUrl() != null && !author.getPhotoUrl().isBlank()) {
                String publicId = CloudinaryService.extractPublicIdFromUrl(author.getPhotoUrl());
                    cloudinaryService.deleteFile(publicId, "image");
            }
        } catch (Exception e) { // Не прерываем удаление автора
            log.error("Ошибка при удалении фото автора из Cloudinary: {}", e.getMessage());
        }

        repository.deleteById(id);
        log.info("Удален автор: '{}' с id = {}", author.getFullName(), id);
    }

    public List<AuthorForSelect> getAuthorsForSelect() {
        return repository.findAllForSelect();
    }
}
