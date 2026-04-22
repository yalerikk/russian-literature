package com.literature.russian_literature.books.api;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.domain.BookFormat;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.catalog.api.dto.BookForCatalogDto;
import com.literature.russian_literature.catalog.db.BookForCatalogMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final BookForCatalogMapper bookForCatalogMapper;

    public BookController(BookService bookService, BookForCatalogMapper bookForCatalogMapper) {
        this.bookService = bookService;
        this.bookForCatalogMapper = bookForCatalogMapper;
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        log.info("Called getBookById by id={}", id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("Called getAllBooks");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // POST
    @PostMapping
    public ResponseEntity<Book> createBook(
            @Valid @RequestBody Book bookToCreate
    ) {
        log.info("Called createBook");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookToCreate));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book bookToUpdate
    ) {
        log.info("Called updateBook id={}, bookToUpdate={}", id, bookToUpdate);
        Book updated = bookService.updateBook(id, bookToUpdate);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id
    ) {
        log.info("Called deleteBook: id={}", id);
        bookService.deleteBook(id);
        return ResponseEntity.ok()
                .build();
    }

    // FILTER
    @GetMapping("/filter")
    public ResponseEntity<Page<BookForCatalogDto>> filterBooks(
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String literature,
            @RequestParam(required = false) String readingType,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("Called filterBooks with genreIds={}, grade={}, level={}, literature={}, readingType={}, " +
                        "categoryCode={}, searchQuery={}, authorId={}, page={}, size={}",
                genreIds, grade, level, literature, readingType, categoryCode, searchQuery, authorId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookService.filterBooks(genreIds, grade, level, literature, readingType,
                categoryCode, searchQuery, authorId, pageable);
        Page<BookForCatalogDto> dtoPage = bookPage.map(bookForCatalogMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    // DOWNLOAD
    @GetMapping("/{id}/download")
    public ResponseEntity<Map<String, String>> getBookFileUrl(
            @PathVariable Long id,
            @RequestParam BookFormat format) {
        String url = bookService.getBookFileUrlByFormat(id, format);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
