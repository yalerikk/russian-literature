package com.literature.russian_literature.ratings.db;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.ratings.domain.BookRating;
import com.literature.russian_literature.users.db.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class BookRatingMapper {

    public BookRating toDomain(BookRatingEntity entity) {
        return new BookRating(
                entity.getId(),
                entity.getBook().getId(),
                entity.getUser().getId(),
                entity.getRating(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public BookRatingEntity toEntity(BookRating rating, BookEntity book, UserEntity user) {
        BookRatingEntity entity = new BookRatingEntity();
        entity.setId(rating.id());
        entity.setBook(book);
        entity.setUser(user);
        entity.setRating(rating.rating());
        return entity;
    }
}