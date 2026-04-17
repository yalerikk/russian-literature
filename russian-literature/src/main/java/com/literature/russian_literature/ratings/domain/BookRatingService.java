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
    private static final Logger log = LoggerFactory.getLogger(BookRatingService.class);

    private final BookRatingRepository ratingRepository;
    private final BookRatingMapper mapper;
    private final BookRatingValidator validator;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookRatingService(BookRatingRepository ratingRepository,
                             BookRatingMapper mapper,
                             BookRatingValidator validator,
                             BookRepository bookRepository,
                             UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Получить оценку пользователя для книги
    public Optional<BookRating> getUserRating(Long bookId, Long userId) {
        return ratingRepository.findByBookIdAndUserId(bookId, userId)
                .map(mapper::toDomain);
    }

    // Получить все оценки пользователя
    public List<BookRating> getUserRatings(Long userId) {
        validator.validateUserExists(userId);
        return ratingRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    // Поставить или обновить оценку
    @Transactional
    public BookRating saveOrUpdateRating(BookRating rating) {
        validator.validateForCreate(rating);

        BookEntity book = bookRepository.findById(rating.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));
        UserEntity user = userRepository.findById(rating.userId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        Optional<BookRatingEntity> existing = ratingRepository.findByBookIdAndUserId(rating.bookId(), rating.userId());

        BookRatingEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.setRating(rating.rating());
            entity.setUpdatedAt(LocalDateTime.now());
            log.info("Обновлена оценка для книги id={} от пользователя id={}: {}", rating.bookId(), rating.userId(), rating.rating());
        } else {
            entity = mapper.toEntity(rating, book, user);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            log.info("Создана новая оценка для книги id={} от пользователя id={}: {}", rating.bookId(), rating.userId(), rating.rating());
        }

        BookRatingEntity saved = ratingRepository.save(entity);
        return mapper.toDomain(saved);
    }

    // Удалить оценку
    @Transactional
    public void deleteRating(Long bookId, Long userId) {
        validator.validateRatingExists(bookId, userId);
        ratingRepository.deleteByBookIdAndUserId(bookId, userId);
        log.info("Удалена оценка для книги id={} от пользователя id={}", bookId, userId);
    }

    // Получить статистику рейтинга книги
    public RatingSummary getBookRatingSummary(Long bookId) {
        validator.validateBookExists(bookId);
        Double avg = ratingRepository.findAverageRatingByBookId(bookId);
        Integer count = ratingRepository.countByBookId(bookId);
        return new RatingSummary(avg != null ? avg : 0.0, count != null ? count : 0);
    }

    // DTO для сводки
    public record RatingSummary(Double averageRating, Integer ratingCount) {}
}
