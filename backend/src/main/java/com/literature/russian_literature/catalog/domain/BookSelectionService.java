package com.literature.russian_literature.catalog.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.catalog.db.BookForCatalogMapper;
import com.literature.russian_literature.catalog.domain.dto.CatalogCategory;
import com.literature.russian_literature.ratings.domain.BookRatingService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookSelectionService {
    private final BookRepository bookRepository;
    private final BookForCatalogMapper mapper;

    public BookSelectionService(BookRepository bookRepository, BookForCatalogMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public List<BookForCatalogDto> getBooksForCategory(CatalogCategory category, Long userId) {
        List<BookEntity> books;

        switch (CatalogCategory.CriteriaType.valueOf(category.criteriaType())) {
            case NEW:
                books = getNewBooks(category);
                break;
            case POPULAR:
                books = getPopularBooks(category);
                break;
            case BY_PERIOD:
                books = getBooksByPeriod(category);
                break;
            case CUSTOM:
                Specification<BookEntity> spec = buildSpecificationFromCustomCategory(category);
                Pageable pageable = PageRequest.of(0, category.booksToShow());
                books = bookRepository.findAll(spec, pageable).getContent();
                break;
            default:
                throw new IllegalArgumentException("Unknown criteria type: " + category.criteriaType());
        }

        int limit = Math.min(category.booksToShow(), books.size());
        return books.stream()
                .limit(limit)
                .map(book -> mapper.toDto(book, userId))
                .collect(Collectors.toList());
    }

    private List<BookEntity> getNewBooks(CatalogCategory category) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(category.daysInterval());
        return bookRepository.findRecentBooks(startDate, category.booksToShow());
    }

    private List<BookEntity> getPopularBooks(CatalogCategory category) {
        return bookRepository.findTopBooksByRating(category.booksToShow());
    }

    private List<BookEntity> getBooksByPeriod(CatalogCategory category) {
        Pageable pageable = PageRequest.of(0, category.booksToShow());
        return bookRepository.findByPublicationYearBetween(
                category.minPublicationYear(),
                category.maxPublicationYear(),
                pageable
        ).getContent();
    }

    private Specification<BookEntity> buildSpecificationFromCustomCategory(CatalogCategory category) {
        Specification<BookEntity> spec = (root, query, cb) -> cb.conjunction();
        if (category.tagIds() != null && !category.tagIds().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                var tagsJoin = root.join("tags");
                return tagsJoin.get("id").in(category.tagIds());
            });
        }
        return spec;
    }

    // PAGE
    public Page<BookEntity> getBooksForCategoryPage(CatalogCategory category, Pageable pageable) {
        switch (CatalogCategory.CriteriaType.valueOf(category.criteriaType())) {
            case NEW:
                return getNewBooksPage(category, pageable);
            case POPULAR:
                return getPopularBooksPage(category, pageable);
            case BY_PERIOD:
                return getBooksByPeriodPage(category, pageable);
            case CUSTOM:
                Specification<BookEntity> spec = buildSpecificationFromCustomCategory(category);
                return bookRepository.findAll(spec, pageable);
            default:
                throw new IllegalArgumentException("Unknown criteria type: " + category.criteriaType());
        }
    }

    private Page<BookEntity> getNewBooksPage(CatalogCategory category, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(category.daysInterval());
        return bookRepository.findByCreatedAtAfterOrderByCreatedAtDesc(startDate, pageable);
    }

    private Page<BookEntity> getPopularBooksPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findTopBooksByRatingPage(pageable);
    }

    private Page<BookEntity> getBooksByPeriodPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findByPublicationYearBetween(
                category.minPublicationYear(),
                category.maxPublicationYear(),
                pageable
        );
    }
}
