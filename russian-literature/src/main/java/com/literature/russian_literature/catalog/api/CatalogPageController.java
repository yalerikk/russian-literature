package com.literature.russian_literature.catalog.api;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.catalog.api.dto.BookForCatalogDto;
import com.literature.russian_literature.catalog.api.dto.CatalogCategoryWithBooksDto;
import com.literature.russian_literature.catalog.api.dto.CatalogPageDto;
import com.literature.russian_literature.catalog.db.BookForCatalogMapper;
import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.catalog.domain.CatalogCategoryService;
import com.literature.russian_literature.catalog.domain.BookSelectionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalog")
public class CatalogPageController {
    private static final Logger log = LoggerFactory.getLogger(CatalogPageController.class);

    private final CatalogCategoryService categoryService;
    private final BookService bookService;
    private final BookSelectionService bookSelectionService;
    private final BookForCatalogMapper bookForCatalogMapper;

    public CatalogPageController(CatalogCategoryService categoryService, BookService bookService,
                                 BookSelectionService bookSelectionService, BookForCatalogMapper bookForCatalogMapper) {
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.bookSelectionService = bookSelectionService;
        this.bookForCatalogMapper = bookForCatalogMapper;
    }

    @GetMapping("/page")
    public ResponseEntity<CatalogPageDto> getCatalogPage() {
        log.info("Called getCatalogPage");

        List<CatalogCategory> activeCategories = categoryService.getActiveCategories();

        List<CatalogCategoryWithBooksDto> categoriesWithBooks = activeCategories.stream()
                .map(category -> {
                    var books = bookSelectionService.getBooksForCategory(category);
                    return new CatalogCategoryWithBooksDto(
                            category.id(),
                            category.name(),
                            category.code(),
                            category.displayOrder(),
                            category.booksToShow(),
                            books
                    );
                })
                .collect(Collectors.toList());

        CatalogPageDto response = new CatalogPageDto(
                categoriesWithBooks,
                categoriesWithBooks.size(),
                false // или true, если есть пагинация
        );

        return ResponseEntity.ok(response);
    }

    // слайдер
    @GetMapping("/category/{code}/books")
    public ResponseEntity<Page<BookForCatalogDto>> getCategoryBooks(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookService.filterBooks(
                null, null, null, null, null, code, null, null, pageable
        );
        return ResponseEntity.ok(bookPage.map(bookForCatalogMapper::toDto));
    }
}
