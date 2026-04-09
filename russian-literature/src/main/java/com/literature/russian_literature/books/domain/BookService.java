package com.literature.russian_literature.books.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookMapper;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.books.util.BookNormalizer;
import com.literature.russian_literature.books.util.BookValidator;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.catalog.domain.BookSelectionService;
import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.tags.db.BookTagEntity;
import com.literature.russian_literature.tags.db.BookTagRepository;
import com.literature.russian_literature.tags.domain.TagType;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;
    private final BookMapper mapper;
    private final BookValidator validator;
    private final BookNormalizer normalizer;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookTagRepository bookTagRepository;
    private final BookSelectionService bookSelectionService;

    public BookService(BookRepository repository, BookMapper mapper,
                       BookValidator validator, BookNormalizer normalizer,
                       AuthorRepository authorRepository, GenreRepository genreRepository,
                       BookTagRepository bookTagRepository, BookSelectionService bookSelectionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookTagRepository = bookTagRepository;
        this.bookSelectionService = bookSelectionService;
    }

    public Book getBookById (
            Long id
    ) {
        BookEntity bookEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Книга с id = " + id + " не найдена"
                ));

        return mapper.toDomain(bookEntity);
    }

    public Page<BookEntity> getBooksByAuthorPage(Long authorId, Pageable pageable) {
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Автор с id = " + authorId + " не найден");
        }
        return repository.findByAuthorId(authorId, pageable);
    }

    public Page<BookEntity> getBooksByGenrePage(Long genreId, Pageable pageable) {
        if (!genreRepository.existsById(genreId)) {
            throw new EntityNotFoundException("Жанр с id = " + genreId + " не найден");
        }
        return repository.findByGenreId(genreId, pageable);
    }

    public Page<BookEntity> getBooksByTagPage(Long tagId, Pageable pageable) {
        if (!bookTagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("Тег с id = " + tagId + " не найден");
        }
        return repository.findByTagId(tagId, pageable);
    }

    public List<Book> getAllBooks() {
        List<BookEntity> allBooks = repository.findAll();

        return allBooks.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public Book createBook(Book bookToCreate) {
        // Нормализация перед валидацией
        Book normalizedBook = normalizer.normalizeBook(bookToCreate);
        validator.validateForCreate(normalizedBook);

        AuthorEntity author = authorRepository.findById(normalizedBook.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Автор с id = " + normalizedBook.authorId() + " не найден"));

        // Загружаем жанры и теги по ID
        var genres = genreRepository.findAllById(normalizedBook.genreIds());
        var tags = bookTagRepository.findAllById(normalizedBook.tagIds());

        // Преобразуем в Set для маппера
        Set<GenreEntity> genreSet = new HashSet<>(genres);
        Set<BookTagEntity> tagSet = new HashSet<>(tags);

        var entityToSave = mapper.toEntity(normalizedBook, author, genreSet, tagSet);

        // Устанавливаем даты создания/обновления
        LocalDateTime now = LocalDateTime.now();
        entityToSave.setCreatedAt(now);
        entityToSave.setUpdatedAt(now);

        var savedEntity = repository.save(entityToSave);
        log.info("Создана книга: '{}' автора {}", savedEntity.getTitle(), author.getFullName());

        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Book updateBook(Long id, Book book) {
        BookEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id = " + id + " не найдена"));

        // Нормализация перед валидацией
        Book normalizedBook = normalizer.normalizeBook(book);
        validator.validateForUpdate(id, normalizedBook);

        AuthorEntity author = authorRepository.findById(normalizedBook.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Автор с id = " + normalizedBook.authorId() + " не найден"));

        // Загружаем жанры и теги по ID
        var genres = genreRepository.findAllById(normalizedBook.genreIds());
        var tags = bookTagRepository.findAllById(normalizedBook.tagIds());

        // Обновляем поля
        existing.setTitle(normalizedBook.title());
        existing.setPublicationYear(normalizedBook.publicationYear());
        existing.setDescription(normalizedBook.description());
        existing.setAuthor(author);
        existing.setExternalFileUrl(normalizedBook.externalFileUrl());
        existing.setCoverUrl(normalizedBook.coverUrl());
        existing.setGenres(new HashSet<>(genres));
        existing.setEducationalTags(new HashSet<>(tags));
        existing.setUpdatedAt(LocalDateTime.now());

        BookEntity updated = repository.save(existing);
        log.info("Обновлена книга: '{}'", updated.getTitle());
        return mapper.toDomain(updated);
    }

    public void deleteBook(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id = " + id + " не найдена"));

        repository.deleteById(id);
        log.info("Удалена книга: '{}' с id = {},", book.getTitle(), id);
    }

    // Новые методы для каталога

    public List<Book> getBooksCreatedAfter(LocalDateTime date, int limit) {
        return repository.findByCreatedAtAfterOrderByCreatedAtDesc(date, limit).stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Page<BookEntity> filterBooks(List<Long> genreIds, String grade, String level,
                                        String literature, String readingType, Pageable pageable) {
        Specification<BookEntity> spec = (root, query, cb) -> cb.conjunction();
        if (genreIds != null && !genreIds.isEmpty()) {
            spec = spec.and(BookSpecifications.byGenres(genreIds));
        }
        spec = spec.and(BookSpecifications.byTagTypeAndName(TagType.GRADE, grade))
                .and(BookSpecifications.byTagTypeAndName(TagType.LEVEL, level))
                .and(BookSpecifications.byTagTypeAndName(TagType.CATEGORY, literature))
                .and(BookSpecifications.byTagTypeAndName(TagType.READING_TYPE, readingType));
        return repository.findAll(spec, pageable);
    }

    public Page<BookEntity> getBooksForCategoryPage(CatalogCategory category, Pageable pageable) {
        return bookSelectionService.getBooksForCategoryPage(category, pageable);
    }
}
