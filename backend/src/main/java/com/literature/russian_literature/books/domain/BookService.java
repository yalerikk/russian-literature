package com.literature.russian_literature.books.domain;

import com.literature.russian_literature.books.db.*;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.books.domain.dto.BookFileResponse;
import com.literature.russian_literature.books.util.BookNormalizer;
import com.literature.russian_literature.books.util.BookValidator;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.catalog.domain.dto.CatalogCategory;
import com.literature.russian_literature.catalog.domain.CatalogCategoryService;
import com.literature.russian_literature.cloudinary.CloudinaryService;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.ratings.db.BookRatingRepository;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagRepository;
import com.literature.russian_literature.tags.domain.TagType;
import com.literature.russian_literature.userbooks.db.UserBookRepository;

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
    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;
    private final BookMapper mapper;
    private final BookValidator validator;
    private final BookNormalizer normalizer;
    private final CatalogCategoryService catalogCategoryService;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final BookFileRepository bookFileRepository;
    private final CloudinaryService cloudinaryService;
    private final UserBookRepository userBookRepository;
    private final BookRatingRepository bookRatingRepository;

    public BookService(BookRepository repository, BookMapper mapper, BookValidator validator,
                       BookNormalizer normalizer, CatalogCategoryService catalogCategoryService,
                       AuthorRepository authorRepository, GenreRepository genreRepository, TagRepository tagRepository,
                       BookFileRepository bookFileRepository, CloudinaryService cloudinaryService,
                       UserBookRepository userBookRepository, BookRatingRepository bookRatingRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.catalogCategoryService = catalogCategoryService;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.bookFileRepository = bookFileRepository;
        this.cloudinaryService = cloudinaryService;
        this.userBookRepository = userBookRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    public Book getBookById(Long id) {
        BookEntity bookEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = " + id + " not found"));
        return mapper.toDomain(bookEntity);
    }

    public String getBookFileUrlByFormat(Long bookId, BookFormat format) {
        BookFileEntity file = bookFileRepository.findByBookIdAndFormat(bookId, format)
                .orElseThrow(() -> new EntityNotFoundException("File of format " + format + " not found for book id=" + bookId));
        return file.getFileUrl();
    }

    public Page<BookEntity> getAllBooks(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public Book createBook(Book bookToCreate) {
        Book normalizedBook = normalizer.normalizeBook(bookToCreate);
        validator.validateForCreate(normalizedBook);

        AuthorEntity author = authorRepository.findById(normalizedBook.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id = " + normalizedBook.authorId() + " not found"));

        var genres = genreRepository.findAllById(normalizedBook.genreIds());
        var tags = tagRepository.findAllById(normalizedBook.tagIds());

        Set<GenreEntity> genreSet = new HashSet<>(genres);
        Set<TagEntity> tagSet = new HashSet<>(tags);

        var entityToSave = mapper.toEntity(normalizedBook, author, genreSet, tagSet);

        LocalDateTime now = LocalDateTime.now();
        entityToSave.setCreatedAt(now);
        entityToSave.setUpdatedAt(now);

        var savedEntity = repository.save(entityToSave);
        LOG.info("Created book: '{}' by author {}", savedEntity.getTitle(), author.getFullName());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Book updateBook(Long id, Book book) {
        BookEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = " + id + " not found"));

        Book normalizedBook = normalizer.normalizeBook(book);
        validator.validateForUpdate(id, normalizedBook);

        AuthorEntity author = authorRepository.findById(normalizedBook.authorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id = " + normalizedBook.authorId() + " not found"));

        var genres = genreRepository.findAllById(normalizedBook.genreIds());
        var tags = tagRepository.findAllById(normalizedBook.tagIds());

        existing.setTitle(normalizedBook.title());
        existing.setPublicationYear(normalizedBook.publicationYear());
        existing.setDescription(normalizedBook.description());
        existing.setAuthor(author);
        existing.setCoverUrl(normalizedBook.coverUrl());
        existing.setGenres(new HashSet<>(genres));
        existing.setTags(new HashSet<>(tags));
        existing.setUpdatedAt(LocalDateTime.now());

        BookEntity updated = repository.save(existing);
        LOG.info("Updated book: '{}'", updated.getTitle());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteBook(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = " + id + " not found"));

        List<BookFileEntity> files = bookFileRepository.findByBookId(id);
        for (BookFileEntity file : files) {
            deleteFileFromBook(id, file.getId());
        }

        try {
            if (book.getCoverUrl() != null && !book.getCoverUrl().isBlank()) {
                String publicId = CloudinaryService.extractPublicIdFromUrl(book.getCoverUrl());
                cloudinaryService.deleteFile(publicId, "image");
                LOG.info("Cover deleted from Cloudinary: {}", publicId);
            }
        } catch (Exception e) {
            LOG.error("Failed to delete cover from Cloudinary for book id={}: {}", id, e.getMessage());
        }

        userBookRepository.deleteByBookId(id);
        bookRatingRepository.deleteByBookId(id);
        repository.deleteById(id);
        LOG.info("Deleted book: '{}' with id = {}", book.getTitle(), id);
    }

    public Page<BookEntity> filterBooks(List<Long> genreIds, String grade, String level,
                                        String literature, String readingType, String categoryCode, String searchQuery, Long authorId,
                                        Pageable pageable
    ) {
        Specification<BookEntity> spec = (root, query, cb) -> cb.conjunction();

        if (genreIds != null && !genreIds.isEmpty()) {
            spec = spec.and((root, q, cb) -> {
                var genresJoin = root.join("genres");
                return genresJoin.get("id").in(genreIds);
            });
        }

        spec = spec.and(BookSpecifications.byTagTypeAndName(TagType.GRADE, grade))
                .and(BookSpecifications.byTagTypeAndName(TagType.LEVEL, level))
                .and(BookSpecifications.byTagTypeAndName(TagType.CATEGORY, literature))
                .and(BookSpecifications.byTagTypeAndName(TagType.READING_TYPE, readingType));

        if (categoryCode != null && !categoryCode.isBlank()) {
            CatalogCategory category = catalogCategoryService.getCategoryByCode(categoryCode);
            if ("POPULAR".equals(category.criteriaType())) {
                List<Long> popularIds = repository.findTopBooksRatingIds(500);
                if (popularIds.isEmpty()) {
                    return Page.empty(pageable);
                }
                spec = spec.and((root, q, cb) -> root.get("id").in(popularIds));
            } else {
                spec = spec.and(buildCategorySpecification(category));
            }
        }

        if (searchQuery != null && !searchQuery.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + searchQuery.toLowerCase() + "%")
            );
        }

        if (authorId != null) {
            spec = spec.and((root, q, cb) ->
                    cb.equal(root.get("author").get("id"), authorId)
            );
        }

        return repository.findAll(spec, pageable);
    }

    public List<BookFileResponse> getFilesByBookId(Long bookId) {
        List<BookFileEntity> files = bookFileRepository.findByBookId(bookId);
        return files.stream()
                .map(f -> new BookFileResponse(f.getId(), f.getFileUrl(), f.getFormat(), f.getPublicId()))
                .toList();
    }

    @Transactional
    public BookFileResponse addFileToBook(Long bookId, String fileUrl, BookFormat format, String publicId) {
        BookEntity book = repository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (bookFileRepository.existsByBookIdAndFormat(bookId, format)) {
            throw new IllegalArgumentException("File of format " + format + " already exists for this book");
        }

        if (format != BookFormat.PDF) {
            boolean hasPdf = bookFileRepository.existsByBookIdAndFormat(bookId, BookFormat.PDF);
            if (!hasPdf) {
                throw new IllegalArgumentException("You must upload PDF file first");
            }
        }

        BookFileEntity file = new BookFileEntity();
        file.setBook(book);
        file.setFileUrl(fileUrl);
        file.setFormat(format);
        file.setPublicId(publicId);
        file.setCreatedAt(LocalDateTime.now());

        BookFileEntity saved = bookFileRepository.save(file);
        LOG.info("Added {} file for book id={}", format, bookId);
        return new BookFileResponse(saved.getId(), saved.getFileUrl(), saved.getFormat(), saved.getPublicId());
    }

    @Transactional
    public void deleteFileFromBook(Long bookId, Long fileId) {
        BookFileEntity file = bookFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        if (!file.getBook().getId().equals(bookId)) {
            throw new IllegalArgumentException("File does not belong to the specified book");
        }

        if (file.getPublicId() != null && !file.getPublicId().isBlank()) {
            try {
                cloudinaryService.deleteFile(file.getPublicId(), "raw");
                LOG.info("Deleted file from Cloudinary: {}", file.getPublicId());
            } catch (Exception e) {
                LOG.error("Failed to delete file from Cloudinary: {}", e.getMessage());
            }
        }

        bookFileRepository.delete(file);
        LOG.info("Deleted file id={} from book id={}", fileId, bookId);
    }

    private Specification<BookEntity> buildCategorySpecification(CatalogCategory category) {
        return switch (category.criteriaType()) {
            case "NEW" -> (root, query, cb) -> {
                LocalDateTime startDate = LocalDateTime.now().minusDays(category.daysInterval());
                return cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            };
            case "BY_PERIOD" -> (root, query, cb) -> {
                if (category.minPublicationYear() != null && category.maxPublicationYear() != null) {
                    return cb.between(root.get("publicationYear"),
                            category.minPublicationYear(), category.maxPublicationYear());
                }
                return cb.conjunction();
            };
            case "CUSTOM" -> (root, query, cb) -> {
                if (category.tagIds() == null || category.tagIds().isEmpty()) {
                    return cb.conjunction();
                }
                var tagsJoin = root.join("tags");
                return tagsJoin.get("id").in(category.tagIds());
            };
            default -> (root, query, cb) -> cb.conjunction();
        };
    }
}
