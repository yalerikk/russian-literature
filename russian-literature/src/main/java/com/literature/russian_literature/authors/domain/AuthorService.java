package com.literature.russian_literature.authors.domain;

import com.literature.russian_literature.authors.db.AuthorMapper;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.authors.db.AuthorEntity;

import com.literature.russian_literature.authors.util.AuthorNormalizer;
import com.literature.russian_literature.authors.util.AuthorValidator;
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
    private final AuthorValidator authorValidator;
    private final AuthorNormalizer normalizer;
    //private final BookService bookService;

    @Autowired
    public AuthorService(AuthorRepository repository, AuthorMapper mapper,
                         AuthorValidator authorValidator, AuthorNormalizer normalizer) {
        this.repository = repository;
        this.mapper = mapper;
        this.authorValidator = authorValidator;
        this.normalizer = normalizer;
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
        authorValidator.validateCreate(normalizedAuthor);

        var entityToSave = mapper.toEntity(normalizedAuthor);
        var savedEntity = repository.save(entityToSave);
        log.info("Создан автор: '{}' с id = {}", savedEntity.getFullName(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Author updateAuthor(Long id, Author author) {
        AuthorEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автор с id = " + id + " не найден"));

        authorValidator.validateUpdate(id, author);

        // Нормализация перед обновлением
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

        repository.deleteById(id);
        log.info("Удален автор: '{}' с id = {}", author.getFullName(), id);
    }

    /*
    //Получить авторов для выпадающего списка
    public List<AuthorForSelect> getAuthorsForSelect() {
        return repository.findAll().stream()
                .map(this::toAuthorForSelect)
                .sorted(Comparator.comparing(AuthorForSelect::fullName))
                .toList();
    }

    //Поиск авторов по части ФИО
    public List<AuthorForSelect> searchAuthors(String query) {
        String searchTerm = query.toLowerCase();
        return repository.findAll().stream()
                .filter(author ->
                        author.getLastName().toLowerCase().contains(searchTerm) ||
                                author.getFirstName().toLowerCase().contains(searchTerm) ||
                                (author.getMiddleName() != null &&
                                        author.getMiddleName().toLowerCase().contains(searchTerm))
                )
                .map(this::toAuthorForSelect)
                .toList();
    }

    //Получить автора с его книгами
    public AuthorWithBooksResponse getAuthorWithBooks(Long id) {
        AuthorEntity author = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автор с id=" + id + " не найден"));

        List<BookInfo> books = bookService.getBooksByAuthor(id);

        return new AuthorWithBooksResponse(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getMiddleName(),
                author.getFullName(),
                author.getBiography(),
                author.getPhotoUrl(),
                books
        );
    }

    private AuthorForSelect toAuthorForSelect(AuthorEntity entity) {
        String fullName = entity.getLastName() + " " +
                entity.getFirstName() +
                (entity.getMiddleName() != null ? " " + entity.getMiddleName() : "");

        String displayName = entity.getLastName() + " " +
                entity.getFirstName().charAt(0) + "." +
                (entity.getMiddleName() != null ? entity.getMiddleName().charAt(0) + "." : "");

        return new AuthorForSelect(entity.getId(), fullName, displayName);
    }
    */
}
