package com.literature.russian_literature.catalog.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.catalog.db.BookForCatalogMapper;
import com.literature.russian_literature.catalog.domain.dto.CatalogCategory;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
        Pageable pageable = PageRequest.of(0, category.booksToShow(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return bookRepository.findAll(pageable).getContent();
    }

    private List<BookEntity> getPopularBooks(CatalogCategory category) {
        return bookRepository.findTopBooksByRating(category.booksToShow());
    }

    private List<BookEntity> getBooksByPeriod(CatalogCategory category) {
        Pageable pageable = PageRequest.of(0, category.booksToShow(), Sort.by(Sort.Direction.DESC, "publicationYear"));
        return bookRepository.findByPublicationYearBetween(
                category.minPublicationYear(),
                category.maxPublicationYear(),
                pageable
        ).getContent();
    }

    private Specification<BookEntity> buildSpecificationFromCustomCategory(CatalogCategory category) {
        Set<Long> tagIds = category.tagIds();
        if (tagIds == null || tagIds.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            query.distinct(true);
            Predicate[] predicates = tagIds.stream()
                    .map(tagId -> {
                        var tagsJoin = root.join("tags");
                        return cb.equal(tagsJoin.get("id"), tagId);
                    })
                    .toArray(Predicate[]::new);
            return cb.and(predicates);
        };
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
        return bookRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    private Page<BookEntity> getPopularBooksPage(CatalogCategory category, Pageable pageable) {
        return bookRepository.findTopBooksByRatingPage(pageable);
    }

    private Page<BookEntity> getBooksByPeriodPage(CatalogCategory category, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "publicationYear")
        );
        return bookRepository.findByPublicationYearBetween(
                category.minPublicationYear(),
                category.maxPublicationYear(),
                sortedPageable
        );
    }
}
