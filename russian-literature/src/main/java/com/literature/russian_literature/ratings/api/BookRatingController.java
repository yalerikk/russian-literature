package com.literature.russian_literature.ratings.api;

import com.literature.russian_literature.ratings.domain.dto.BookRating;
import com.literature.russian_literature.ratings.domain.BookRatingService;
import com.literature.russian_literature.ratings.domain.dto.BookRatingRequest;
import com.literature.russian_literature.security.SecurityUtils;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.domain.UserRole;
import com.literature.russian_literature.users.domain.UserService;

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
    private final UserService userService;

    public BookRatingController(BookRatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @GetMapping("/book/{bookId}/summary")
    public ResponseEntity<BookRatingService.RatingSummary> getBookRatingSummary(
            @PathVariable Long bookId
    ) {
        log.info("Get rating summary for bookId={}", bookId);
        return ResponseEntity.ok(ratingService.getBookRatingSummary(bookId));
    }

    @GetMapping("/book/{bookId}/my-rating")
    public ResponseEntity<BookRating> getMyRating(
            @PathVariable Long bookId
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        log.info("Get my rating for bookId={}, userId={}", bookId, currentUserId);
        return ratingService.getUserRating(bookId, currentUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookRating>> getUserRatings(
            @PathVariable Long userId
    ) {
        log.info("Get user ratings for userId={}", userId);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userService.getUserEntityById(currentUserId);
        if (!currentUser.getRole().equals(UserRole.ADMIN) && !currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(ratingService.getUserRatings(userId));
    }

    @PostMapping
    public ResponseEntity<BookRating> saveRating(
            @Valid @RequestBody BookRatingRequest request
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        BookRating rating = new BookRating(null, request.bookId(), currentUserId, request.rating(), null, null);
        BookRating saved = ratingService.saveOrUpdateRating(rating);
        log.info("Saved rating for bookId={}, userId={}, rating={}", saved.bookId(), saved.userId(), saved.rating());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteMyRating(
            @PathVariable Long bookId
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        ratingService.deleteRating(bookId, currentUserId);
        log.info("Deleted rating for bookId={}, userId={}", bookId, currentUserId);
        return ResponseEntity.ok().build();
    }
}
