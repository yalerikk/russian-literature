package com.literature.russian_literature.ratings.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.ratings.db.BookRatingEntity;
import com.literature.russian_literature.ratings.db.BookRatingMapper;
import com.literature.russian_literature.ratings.db.BookRatingRepository;
import com.literature.russian_literature.ratings.domain.dto.BookRating;
import com.literature.russian_literature.ratings.util.BookRatingValidator;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.db.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookRatingService {
    private static final Logger LOG = LoggerFactory.getLogger(BookRatingService.class);

    private final BookRatingRepository ratingRepository;
    private final BookRatingMapper mapper;
    private final BookRatingValidator validator;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookRatingService(BookRatingRepository ratingRepository, BookRatingMapper mapper,
                             BookRatingValidator validator, BookRepository bookRepository,
                             UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Optional<BookRating> getUserRating(Long bookId, Long userId) {
        return ratingRepository.findByBookIdAndUserId(bookId, userId)
                .map(mapper::toDomain);
    }

    public List<BookRating> getUserRatings(Long userId) {
        validator.validateUserExists(userId);
        return ratingRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookRating saveOrUpdateRating(BookRating rating) {
        validator.validateForCreate(rating);

        BookEntity book = bookRepository.findById(rating.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        UserEntity user = userRepository.findById(rating.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<BookRatingEntity> existing = ratingRepository.findByBookIdAndUserId(rating.bookId(), rating.userId());

        BookRatingEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.setRating(rating.rating());
            entity.setUpdatedAt(LocalDateTime.now());
            LOG.info("Updated rating for bookId={}, userId={}, rating={}", rating.bookId(), rating.userId(), rating.rating());
        } else {
            entity = mapper.toEntity(rating, book, user);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            LOG.info("Created new rating for bookId={}, userId={}, rating={}", rating.bookId(), rating.userId(), rating.rating());
        }

        BookRatingEntity saved = ratingRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Transactional
    public void deleteRating(Long bookId, Long userId) {
        validator.validateRatingExists(bookId, userId);
        ratingRepository.deleteByBookIdAndUserId(bookId, userId);
        LOG.info("Deleted rating for bookId={}, userId={}", bookId, userId);
    }

    public RatingSummary getBookRatingSummary(Long bookId) {
        validator.validateBookExists(bookId);
        Double avg = ratingRepository.findAverageRatingByBookId(bookId);
        Integer count = ratingRepository.countByBookId(bookId);
        return new RatingSummary(avg != null ? avg : 0.0, count != null ? count : 0);
    }

    public record RatingSummary(Double averageRating, Integer ratingCount) {
    }
}
