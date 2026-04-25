package com.literature.russian_literature.search.api;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.catalog.db.BookForCatalogMapper;
import com.literature.russian_literature.search.domain.SearchSuggestion;
import com.literature.russian_literature.search.domain.SearchService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    private final BookForCatalogMapper bookForCatalogMapper;
    private final BookService bookService;

    public SearchController(SearchService searchService, BookForCatalogMapper bookForCatalogMapper,
                            BookService bookService) {
        this.searchService = searchService;
        this.bookForCatalogMapper = bookForCatalogMapper;
        this.bookService = bookService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<SearchSuggestion>> suggest(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(searchService.suggest(query));
    }

    @GetMapping("/books")
    public ResponseEntity<Page<BookForCatalogDto>> searchBooks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookService.filterBooks(
                null, null, null, null, null, null, query, null, pageable
        );
        return ResponseEntity.ok(bookPage.map(bookForCatalogMapper::toDto));
    }

    @GetMapping("/authors")
    public ResponseEntity<Page<AuthorEntity>> searchAuthors(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(searchService.searchAuthors(query, pageable));
    }
}
