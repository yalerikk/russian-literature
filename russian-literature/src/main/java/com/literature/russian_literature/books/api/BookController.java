package com.literature.russian_literature.books.api;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookMapper;
import com.literature.russian_literature.books.domain.BookFormat;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
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

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final BookForCatalogMapper bookForCatalogMapper;
    private final BookMapper mapper;

    public BookController(BookService bookService, BookForCatalogMapper bookForCatalogMapper, BookMapper mapper) {
        this.bookService = bookService;
        this.bookForCatalogMapper = bookForCatalogMapper;
        this.mapper = mapper;
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<Book>> getBooksForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookService.getAllBooks(pageable);
        Page<Book> dtoPage = bookPage.map(mapper::toDomain);
        LOG.info("Admin list: page={}, size={}, total={}", page, size, dtoPage.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

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
        LOG.info("Called filterBooks with genreIds={}, grade={}, level={}, literature={}, readingType={}, " +
                        "categoryCode={}, searchQuery={}, authorId={}, page={}, size={}",
                genreIds, grade, level, literature, readingType, categoryCode, searchQuery, authorId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookService.filterBooks(genreIds, grade, level, literature, readingType,
                categoryCode, searchQuery, authorId, pageable);
        Page<BookForCatalogDto> dtoPage = bookPage.map(bookForCatalogMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable("id") Long id
    ) {
        LOG.info("Called getBookById by id={}", id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/{id}/read")
    public ResponseEntity<Void> readBook(
            @PathVariable Long id,
            @RequestParam BookFormat format
    ) {
        if (format != BookFormat.PDF) {
            LOG.warn("Unsupported read format {} for book {}", format, id);
            throw new IllegalArgumentException("Online reading is supported only for PDF format");
        }
        String url = bookService.getBookFileUrlByFormat(id, format);
        LOG.info("Redirect {} to read book {}", url, id);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Map<String, String>> getBookFileUrl(
            @PathVariable Long id,
            @RequestParam BookFormat format
    ) {
        String url = bookService.getBookFileUrlByFormat(id, format);
        LOG.info("Provide download URL for book id={}, format={}", id, format);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(
            @Valid @RequestBody Book bookToCreate
    ) {
        LOG.info("Called createBook");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book bookToUpdate
    ) {
        LOG.info("Called updateBook id={}, bookToUpdate={}", id, bookToUpdate);
        Book updated = bookService.updateBook(id, bookToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id
    ) {
        LOG.info("Called deleteBook id={}", id);
        bookService.deleteBook(id);
        return ResponseEntity.ok()
                .build();
    }
}
