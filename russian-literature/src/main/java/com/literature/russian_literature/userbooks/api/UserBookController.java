package com.literature.russian_literature.userbooks.api;

import com.literature.russian_literature.security.SecurityUtils;
import com.literature.russian_literature.userbooks.domain.BookStatus;
import com.literature.russian_literature.userbooks.domain.UserBookService;
import com.literature.russian_literature.userbooks.domain.UserBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/me/books")
public class UserBookController {
    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }

    // Получить список книг по статусу (пагинация)
    @GetMapping
    public ResponseEntity<Page<UserBook>> getUserBooks(
            @RequestParam BookStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userBookService.getUserBooksByStatus(currentUserId, status, pageable));
    }

    // Получить первые N книг для слайдера (например, для страницы "Мои книги")
    @GetMapping("/recent")
    public ResponseEntity<List<UserBook>> getRecentBooks(
            @RequestParam BookStatus status,
            @RequestParam(defaultValue = "7") int limit
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userBookService.getRecentUserBooksByStatus(currentUserId, status, limit));
    }

    // Добавить книгу в коллекцию с указанным статусом (или изменить статус)
    @PostMapping("/{bookId}")
    public ResponseEntity<UserBook> addBookToCollection(
            @PathVariable Long bookId,
            @RequestParam BookStatus status
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserBook response = userBookService.addOrUpdateBookStatus(currentUserId, bookId, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{bookId}/status")
    public ResponseEntity<UserBook> changeStatus(
            @PathVariable Long bookId,
            @RequestParam BookStatus newStatus
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserBook response = userBookService.changeBookStatus(userId, bookId, newStatus);
        return ResponseEntity.ok(response);
    }

    // Удалить книгу из коллекции
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeBookFromCollection(
            @PathVariable Long bookId,
            @RequestParam BookStatus status
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userBookService.removeBookFromCollection(currentUserId, bookId, status);
        return ResponseEntity.noContent().build();
    }

    // Проверить, находится ли книга в избранном (для сердечка)
    @GetMapping("/{bookId}/favorite")
    public ResponseEntity<Map<String, Boolean>> isBookFavorite(@PathVariable Long bookId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        boolean isFavorite = userBookService.isBookInFavorite(currentUserId, bookId);
        return ResponseEntity.ok(Map.of("favorite", isFavorite));
    }

    @PatchMapping("/{bookId}/progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long bookId,
            @RequestParam Integer progress
    ) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userBookService.updateProgress(currentUserId, bookId, progress);
        return ResponseEntity.ok().build();
    }
}
