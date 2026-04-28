package com.literature.russian_literature.catalog.db;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.ratings.domain.BookRatingService;

import org.springframework.stereotype.Component;

@Component
public class BookForCatalogMapper {
    private final BookRatingService ratingService;

    public BookForCatalogMapper(BookRatingService ratingService) {
        this.ratingService = ratingService;
    }

    public BookForCatalogDto toDto(BookEntity book) {
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
