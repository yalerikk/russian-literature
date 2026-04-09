package com.literature.russian_literature.ratings.api;

import com.literature.russian_literature.ratings.domain.BookRating;
import com.literature.russian_literature.ratings.domain.BookRatingService;
// import com.literature.russian_literature.security.SecurityUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class BookRatingController {
    private static final Logger log = LoggerFactory.getLogger(BookRatingController.class);

    private final BookRatingService ratingService;

    public BookRatingController(BookRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/book/{bookId}/summary")
    public ResponseEntity<BookRatingService.RatingSummary> getBookRatingSummary(@PathVariable Long bookId) {
        log.info("Called getBookRatingSummary for bookId={}", bookId);
        return ResponseEntity.ok(ratingService.getBookRatingSummary(bookId));
    }

    // ВРЕМЕННО: возвращает оценку для тестового пользователя (id=1)
    @GetMapping("/book/{bookId}/my-rating")
    public ResponseEntity<BookRating> getMyRating(@PathVariable Long bookId) {
        // Long currentUserId = SecurityUtils.getCurrentUserId(); // TODO: раскомментировать после настройки Security
        Long currentUserId = 1L; // временный тестовый ID
        log.info("Called getMyRating for bookId={}, userId={}", bookId, currentUserId);
        return ratingService.getUserRating(bookId, currentUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookRating>> getBookRatings(@PathVariable Long bookId) {
        log.info("Called getBookRatings for bookId={}", bookId);
        return ResponseEntity.ok(ratingService.getBookRatings(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookRating>> getUserRatings(@PathVariable Long userId) {
        log.info("Called getUserRatings for userId={}", userId);
        return ResponseEntity.ok(ratingService.getUserRatings(userId));
    }

    // ВРЕМЕННО: проверка userId не выполняется, доверяем тому, что прислал клиент
    @PostMapping
    public ResponseEntity<BookRating> saveRating(@Valid @RequestBody BookRating rating) {
        // Long currentUserId = SecurityUtils.getCurrentUserId();
        // if (!rating.userId().equals(currentUserId)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        // }
        log.info("Called saveRating for bookId={}, userId={}, rating={}", rating.bookId(), rating.userId(), rating.rating());
        BookRating saved = ratingService.saveOrUpdateRating(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ВРЕМЕННО: удаляем по переданному userId из тела запроса (или через параметр)
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteMyRating(@PathVariable Long bookId,
                                               @RequestParam Long userId) {
        // Long currentUserId = SecurityUtils.getCurrentUserId();
        Long currentUserId = userId; // временно берём из параметра
        log.info("Called deleteMyRating for bookId={}, userId={}", bookId, currentUserId);
        ratingService.deleteRating(bookId, currentUserId);
        return ResponseEntity.ok().build();
    }
}
