package com.literature.russian_literature.userbooks.db;

import com.literature.russian_literature.ratings.domain.BookRatingService;
import com.literature.russian_literature.userbooks.domain.UserBook;

import org.springframework.stereotype.Component;

@Component
public class UserBookMapper {
    private final BookRatingService ratingService;

    public UserBookMapper(BookRatingService ratingService) {
        this.ratingService = ratingService;
    }

    public UserBook toDomain(UserBookEntity entity) {
        var summary = ratingService.getBookRatingSummary(entity.getBook().getId());
        return new UserBook(
                entity.getId(),
                entity.getBook().getId(),
                entity.getBook().getTitle(),
                entity.getBook().getCoverUrl(),
                entity.getBook().getAuthor().getShortName(),
                entity.getStatus(),
                entity.getProgress(),
                entity.isFavorite(),
                summary.averageRating(),
                summary.ratingCount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
