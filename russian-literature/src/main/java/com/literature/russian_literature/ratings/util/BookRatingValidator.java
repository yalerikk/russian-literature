package com.literature.russian_literature.ratings.util;

import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.ratings.db.BookRatingRepository;
import com.literature.russian_literature.ratings.domain.dto.BookRating;
import com.literature.russian_literature.users.db.UserRepository;

import org.springframework.stereotype.Component;

@Component
public class BookRatingValidator {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookRatingRepository ratingRepository;

    public BookRatingValidator(BookRepository bookRepository,
                               UserRepository userRepository,
                               BookRatingRepository ratingRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public void validateForCreate(BookRating rating) {
        validateBookExists(rating.bookId());
        validateUserExists(rating.userId());
        validateRatingValue(rating.rating());
    }

    public void validateRatingExists(Long bookId, Long userId) {
        if (!ratingRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new IllegalArgumentException("Rating for bookId=" + bookId + ", userId=" + userId + " not found");
        }
    }

    public void validateBookExists(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new IllegalArgumentException("Book with id = " + bookId + " does not exist");
        }
    }

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id = " + userId + " does not exist");
        }
    }

    private void validateRatingValue(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be an integer between 1 and 5");
        }
    }
}
