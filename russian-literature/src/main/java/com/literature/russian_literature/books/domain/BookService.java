package com.literature.russian_literature.books.domain;

import com.literature.russian_literature.books.db.*;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.books.domain.dto.BookFileResponse;
import com.literature.russian_literature.books.util.BookNormalizer;
import com.literature.russian_literature.books.util.BookValidator;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.catalog.domain.BookSelectionService;
import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.cloudinary.CloudinaryService;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagRepository;
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
    private final TagRepository tagRepository;
    private final BookSelectionService bookSelectionService;
    private final BookFileRepository bookFileRepository;
    private final CloudinaryService cloudinaryService;

    public BookService(BookRepository repository, BookMapper mapper,
                       BookValidator validator, BookNormalizer normalizer,
                       AuthorRepository authorRepository, GenreRepository genreRepository,
                       TagRepository tagRepository, BookSelectionService bookSelectionService,
                       BookFileRepository bookFileRepository, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.bookSelectionService = bookSelectionService;
        this.bookFileRepository = bookFileRepository;
        this.cloudinaryService = cloudinaryService;
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
        return repository.findByGenres_Id(genreId, pageable);
    }

    public Page<BookEntity> getBooksByTagPage(Long tagId, Pageable pageable) {
        if (!tagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("Тег с id = " + tagId + " не найден");
        }
        return repository.findByTags_Id(tagId, pageable);
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
        var tags = tagRepository.findAllById(normalizedBook.tagIds());

        // Преобразуем в Set для маппера
        Set<GenreEntity> genreSet = new HashSet<>(genres);
        Set<TagEntity> tagSet = new HashSet<>(tags);

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
        var tags = tagRepository.findAllById(normalizedBook.tagIds());

        // Обновляем поля
        existing.setTitle(normalizedBook.title());
        existing.setPublicationYear(normalizedBook.publicationYear());
        existing.setDescription(normalizedBook.description());
        existing.setAuthor(author);
        existing.setCoverUrl(normalizedBook.coverUrl());
        existing.setGenres(new HashSet<>(genres));
        existing.setTags(new HashSet<>(tags));
        existing.setUpdatedAt(LocalDateTime.now());

        BookEntity updated = repository.save(existing);
        log.info("Обновлена книга: '{}'", updated.getTitle());
        return mapper.toDomain(updated);
    }

    public void deleteBook(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id = " + id + " не найдена"));

        // 1. Удаляем все файлы книги (из БД и Cloudinary)
        List<BookFileEntity> files = bookFileRepository.findByBookId(id);
        for (BookFileEntity file : files) {
            deleteFileFromBook(id, file.getId());
        }

        // 2. Удаляем обложку из Cloudinary, если она есть
        try {
            if (book.getCoverUrl() != null && !book.getCoverUrl().isBlank()) {
                String publicId = CloudinaryService.extractPublicIdFromUrl(book.getCoverUrl());
                cloudinaryService.deleteFile(publicId, "image");
                log.info("Обложка книги удалена из Cloudinary: {}", publicId);
            }
        } catch (Exception e) { // Не прерываем удаление книги
            log.error("Ошибка при удалении файлов из Cloudinary для книги id={}: {}", id, e.getMessage());
        }

        repository.deleteById(id);
        log.info("Удалена книга: '{}' с id = {},", book.getTitle(), id);
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

    public String getBookFileUrlByFormat(Long bookId, BookFormat format) {
        BookFileEntity file = bookFileRepository.findByBookIdAndFormat(bookId, format)
                .orElseThrow(() -> new EntityNotFoundException("Файл формата " + format + " не найден для книги id=" + bookId));
        return file.getFileUrl();
    }

    @Transactional
    public BookFileResponse addFileToBook(Long bookId, String fileUrl, BookFormat format, String publicId) {
        BookEntity book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        // Проверка на дублирование формата
        if (bookFileRepository.existsByBookIdAndFormat(bookId, format)) {
            throw new IllegalArgumentException("Файл формата " + format + " уже существует для этой книги");
        }

        BookFileEntity file = new BookFileEntity();
        file.setBook(book);
        file.setFileUrl(fileUrl);
        file.setFormat(format);
        file.setPublicId(publicId);
        file.setCreatedAt(LocalDateTime.now());

        BookFileEntity saved = bookFileRepository.save(file);
        return new BookFileResponse(saved.getId(), saved.getFileUrl(), saved.getFormat(), saved.getPublicId());
    }

    @Transactional
    public void deleteFileFromBook(Long bookId, Long fileId) {
        BookFileEntity file = bookFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("Файл не найден"));

        if (!file.getBook().getId().equals(bookId)) {
            throw new IllegalArgumentException("Файл не принадлежит указанной книге");
        }

        // Удаляем из Cloudinary, если есть publicId
        if (file.getPublicId() != null && !file.getPublicId().isBlank()) {
            try {
                cloudinaryService.deleteFile(file.getPublicId(), "raw");
                log.info("Файл удалён из Cloudinary: {}", file.getPublicId());
            } catch (Exception e) { // Не прерываем удаление из БД
                log.error("Ошибка при удалении файла из Cloudinary: {}", e.getMessage());
            }
        }

        bookFileRepository.delete(file);
        log.info("Файл (id={}) удалён из БД для книги id={}", fileId, bookId);
    }

    public List<BookFileResponse> getFilesByBookId(Long bookId) {
        List<BookFileEntity> files = bookFileRepository.findByBookId(bookId);
        return files.stream()
                .map(f -> new BookFileResponse(f.getId(), f.getFileUrl(), f.getFormat(), f.getPublicId()))
                .toList();
    }
}
