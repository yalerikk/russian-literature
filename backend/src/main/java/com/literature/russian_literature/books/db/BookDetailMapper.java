package com.literature.russian_literature.books.db;

import com.literature.russian_literature.books.domain.dto.BookDetailDto;
import com.literature.russian_literature.ratings.domain.BookRatingService;
import org.springframework.stereotype.Component;

@Component
public class BookDetailMapper {
    private final BookRatingService ratingService;

    public BookDetailMapper(BookRatingService ratingService) {
        this.ratingService = ratingService;
    }

    public BookDetailDto toDto(BookEntity book) {
        var summary = ratingService.getBookRatingSummary(book.getId());
        return new BookDetailDto(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getCoverUrl(),
                new BookDetailDto.AuthorInfo(book.getAuthor().getId(), book.getAuthor().getShortName()),
                book.getGenres().stream().map(g -> new BookDetailDto.GenreInfo(g.getId(), g.getName())).toList(),
                book.getTags().stream().map(t -> new BookDetailDto.TagInfo(t.getId(), t.getName(), t.getType().name())).toList(),
                summary.averageRating(),
                summary.ratingCount(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
