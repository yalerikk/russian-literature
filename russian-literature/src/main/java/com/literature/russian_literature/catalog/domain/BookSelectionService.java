package com.literature.russian_literature.catalog.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.catalog.api.dto.BookForCatalogDto;
import com.literature.russian_literature.ratings.domain.BookRatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookSelectionService {
    private final BookRepository bookRepository;
    private final BookRatingService ratingService;

    public BookSelectionService(BookRepository bookRepository, BookRatingService ratingService) {
        this.bookRepository = bookRepository;
        this.ratingService = ratingService;
    }

    // Для главной страницы – первые booksToShow книг (List)
    public List<BookForCatalogDto> getBooksForCategory(CatalogCategory category) {
        List<BookEntity> books;

        switch (CatalogCategory.CriteriaType.valueOf(category.criteriaType())) {
            case NEW:
                books = getNewBooks(category);
                break;
            case POPULAR:
                books = getPopularBooks(category);
                break;
            case BY_GENRE:
                books = getBooksByGenre(category);
                break;
            case BY_AUTHOR:
                books = getBooksByAuthor(category);
                break;
            case BY_PERIOD:
                books = getBooksByPeriod(category);
                break;
            case CUSTOM:
                books = getBooksByCustomQuery(category);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип критерия: " + category.criteriaType());
        }

        int limit = Math.min(category.booksToShow(), books.size());
        return books.stream()
                .limit(limit)
                .map(this::toBookForCatalogDto)
                .collect(Collectors.toList());
    }

    // Для бесконечной карусели – Page с пагинацией
    public Page<BookEntity> getBooksForCategoryPage(CatalogCategory category, Pageable pageable) {
        switch (CatalogCategory.CriteriaType.valueOf(category.criteriaType())) {
            case NEW:
                return getNewBooksPage(category, pageable);
            case POPULAR:
                return getPopularBooksPage(category, pageable);
            case BY_GENRE:
                return getBooksByGenrePage(category, pageable);
            case BY_AUTHOR:
                return getBooksByAuthorPage(category, pageable);
            case BY_PERIOD:
                return getBooksByPeriodPage(category, pageable);
            case CUSTOM:
                return getBooksByCustomQueryPage(category, pageable);
            default:
                throw new IllegalArgumentException("Неизвестный тип критерия: " + category.criteriaType());
        }
    }

    // ---- Вспомогательные методы для List (главная) ----
    private List<BookEntity> getNewBooks(CatalogCategory category) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(category.daysInterval());
        return bookRepository.findRecentBooks(startDate, category.booksToShow());
    }

    private List<BookEntity> getPopularBooks(CatalogCategory category) {
        return bookRepository.findTopBooksByRating(category.booksToShow());
    }

    private List<BookEntity> getBooksByGenre(CatalogCategory category) {
        Pageable pageable = PageRequest.of(0, category.booksToShow());
        return bookRepository.findByGenres_Id(category.genreId(), pageable).getContent();
    }

    private List<BookEntity> getBooksByAuthor(CatalogCategory category) {
        Pageable pageable = PageRequest.of(0, category.booksToShow());
        return bookRepository.findByAuthorId(category.authorId(), pageable).getContent();
    }

    private List<BookEntity> getBooksByPeriod(CatalogCategory category) {
        Pageable pageable = PageRequest.of(0, category.booksToShow());
        return bookRepository.findByPublicationYearBetween(
                category.minPublicationYear(),
                category.maxPublicationYear(),
                pageable
        ).getContent();
    }

    private List<BookEntity> getBooksByCustomQuery(CatalogCategory category) {
        return bookRepository.findRandomBooks(category.booksToShow());
    }

    // ---- Вспомогательные методы для Page (пагинация) ----
    private Page<BookEntity> getNewBooksPage(CatalogCategory category, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(category.daysInterval());
        return bookRepository.findByCreatedAtAfterOrderByCreatedAtDesc(startDate, pageable);
    }

    private Page<BookEntity> getPopularBooksPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findTopBooksByRating(pageable);
    }

    private Page<BookEntity> getBooksByGenrePage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findByGenres_Id(category.genreId(), pageable);
    }

    private Page<BookEntity> getBooksByAuthorPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findByAuthorId(category.authorId(), pageable);
    }

    private Page<BookEntity> getBooksByPeriodPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findByPublicationYearBetween(
                category.minPublicationYear(),
                category.maxPublicationYear(),
                pageable
        );
    }

    private Page<BookEntity> getBooksByCustomQueryPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findRandomBooksPage(pageable);
    }

    // ---- Маппинг в DTO ----
    private BookForCatalogDto toBookForCatalogDto(BookEntity book) {
        String authorFullName = "";
        String authorShortName = "";
        if (book.getAuthor() != null) {
            authorFullName = book.getAuthor().getFullName();
            authorShortName = book.getAuthor().getShortName();
        }

        var summary = ratingService.getBookRatingSummary(book.getId());
        Double rating = summary.averageRating();
        Integer ratingCount = summary.ratingCount();

        return new BookForCatalogDto(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getAuthor() != null ? book.getAuthor().getId() : null,
                authorFullName,
                authorShortName,
                book.getCoverUrl(),
                book.getCreatedAt(),
                rating,
                ratingCount
        );
    }
}
