package com.literature.russian_literature.catalog.db;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.ratings.domain.BookRatingService;
import com.literature.russian_literature.userbooks.db.UserBookRepository;

import org.springframework.stereotype.Component;

@Component
public class BookForCatalogMapper {
    private final BookRatingService ratingService;
    private final UserBookRepository userBookRepository;

    public BookForCatalogMapper(BookRatingService ratingService, UserBookRepository userBookRepository) {
        this.ratingService = ratingService;
        this.userBookRepository = userBookRepository;
    }

    public BookForCatalogDto toDto(BookEntity book, Long userId) {
        String authorShortName = book.getAuthor() != null ? book.getAuthor().getShortName() : "";

        var summary = ratingService.getBookRatingSummary(book.getId());
        Double rating = summary.averageRating();
        Integer ratingCount = summary.ratingCount();

        boolean favorite = false;
        if (userId != null) {
            favorite = userBookRepository.existsFavoriteByUserIdAndBookId(userId, book.getId());
        }

        return new BookForCatalogDto(
                book.getId(),
                book.getTitle(),
                book.getCoverUrl(),
                authorShortName,
                rating,
                ratingCount,
                favorite
        );
    }
}
