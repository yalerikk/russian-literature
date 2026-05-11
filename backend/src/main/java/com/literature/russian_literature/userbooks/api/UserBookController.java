package com.literature.russian_literature.userbooks.api;

import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.security.SecurityUtils;
import com.literature.russian_literature.userbooks.domain.BookStatus;
import com.literature.russian_literature.userbooks.domain.UserBookService;
import com.literature.russian_literature.userbooks.domain.UserBook;
import com.literature.russian_literature.userbooks.domain.UserBookStatusResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/me/books")
public class UserBookController {
    private static final Logger LOG = LoggerFactory.getLogger(UserBookController.class);

    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }

    // Получить список книг по статусу (пагинация)
    @GetMapping
    public ResponseEntity<Page<BookForCatalogDto>> getUserBooks(
            @RequestParam BookStatus status,
            @RequestParam(required = false) String genreIds,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String literature,
            @RequestParam(required = false) String readingType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        LOG.info("Filtering user books: status={}, userId={}, genreIds={}, grade={}, level={}, literature={}, readingType={}",
                status, userId, genreIds, grade, level, literature, readingType);
        Page<BookForCatalogDto> books = userBookService.getUserBooksWithFilters(
                userId, status, genreIds, grade, level, literature, readingType, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<BookForCatalogDto>> getFavorites(
            @RequestParam(required = false) String genreIds,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String literature,
            @RequestParam(required = false) String readingType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        LOG.info("Filtering favorites: userId={}, genreIds={}, grade={}, level={}, literature={}, readingType={}",
                userId, genreIds, grade, level, literature, readingType);
        Page<BookForCatalogDto> books = userBookService.getFavoritesWithFilters(
                userId, genreIds, grade, level, literature, readingType, pageable);
        return ResponseEntity.ok(books);
    }

    // GET /users/me/books/slider?status=FAVORITE&page=0&size=7
    @GetMapping("/slider")
    public ResponseEntity<Page<UserBook>> getUserBooksSlider(
            @RequestParam BookStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        LOG.info("Get slider books by status={}, userId={}, page={}, size={}", status, userId, page, size);
        return ResponseEntity.ok(userBookService.getUserBooksByStatus(userId, status, pageable));
    }

    // Получить все статусы книги (для страницы книги)
    @GetMapping("/{bookId}/status")
    public ResponseEntity<UserBookStatusResponse> getBookStatus(
            @PathVariable Long bookId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userBookService.getBookStatus(userId, bookId));
    }

    @GetMapping("/{bookId}/progress")
    public ResponseEntity<Map<String, Integer>> getProgress(
            @PathVariable Long bookId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Integer progress = userBookService.getProgress(userId, bookId);
        LOG.info("Get progress for bookId={}, userId={}, progress={}", bookId, userId, progress);
        return ResponseEntity.ok(Map.of("progress", progress != null ? progress : 0));
    }

    // Проверить, находится ли книга в избранном (для сердечка)
    @GetMapping("/{bookId}/favorite")
    public ResponseEntity<Map<String, Boolean>> isBookFavorite(
            @PathVariable Long bookId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean isFavorite = userBookService.isBookInFavorite(userId, bookId);
        LOG.info("Check favorite for bookId={}, userId={}, isFavorite={}", bookId, userId, isFavorite);
        return ResponseEntity.ok(Map.of("favorite", isFavorite));
    }

    // Добавить/обновить статус и/или избранное
    @PostMapping("/{bookId}")
    public ResponseEntity<UserBook> addBookToCollection(
            @PathVariable Long bookId,
            @RequestParam(required = false) BookStatus status,
            @RequestParam(required = false) Boolean favorite
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        LOG.info("Add book to collection: bookId={}, userId={}, status={}, favorite={}", bookId, userId, status, favorite);
        UserBook result = userBookService.addOrUpdateBookStatus(userId, bookId, status, favorite);
        if (result == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Обновить прогресс (только для READING)
    @PatchMapping("/{bookId}/progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long bookId,
            @RequestParam Integer progress
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        LOG.info("Update progress: bookId={}, userId={}, progress={}", bookId, userId, progress);
        userBookService.updateProgress(userId, bookId, progress);
        return ResponseEntity.ok().build();
    }

    // Удалить книгу из коллекции
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeBookFromCollection(
            @PathVariable Long bookId,
            @RequestParam(required = false) BookStatus status,
            @RequestParam(required = false) Boolean favorite
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        LOG.info("Remove book from collection: bookId={}, userId={}, status={}, favorite={}", bookId, userId, status, favorite);
        userBookService.removeFromCollection(userId, bookId, status, favorite);
        return ResponseEntity.noContent().build();
    }
}
