package com.literature.russian_literature.books.api;

import com.literature.russian_literature.books.domain.BookFormat;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.books.domain.dto.BookFileResponse;
import com.literature.russian_literature.cloudinary.CloudinaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books/{bookId}/files")
public class BookFileController {
    private static final Logger log = LoggerFactory.getLogger(BookFileController.class);

    private final BookService bookService;
    private final CloudinaryService cloudinaryService;

    public BookFileController(BookService bookService, CloudinaryService cloudinaryService) {
        this.bookService = bookService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public ResponseEntity<List<BookFileResponse>> getFiles(@PathVariable Long bookId) {
        log.info("Получение файлов для книги id={}", bookId);
        return ResponseEntity.ok(bookService.getFilesByBookId(bookId));
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") BookFormat format
    ) {
        try {
            String url = cloudinaryService.uploadFile(file, "books");
            String publicId = CloudinaryService.extractPublicIdFromUrl(url);
            BookFileResponse response = bookService.addFileToBook(bookId, url, format, publicId);
            log.info("Файл {} для книги {} загружен", format, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Ошибка загрузки файла для книги {}: {}", bookId, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long bookId, @PathVariable Long fileId) {
        log.info("Удаление файла {} для книги {}", fileId, bookId);
        bookService.deleteFileFromBook(bookId, fileId);
        return ResponseEntity.noContent().build();
    }
}
