package com.literature.russian_literature.catalog.api;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.catalog.domain.dto.BookForCatalogDto;
import com.literature.russian_literature.catalog.domain.dto.CatalogCategoryWithBooksDto;
import com.literature.russian_literature.catalog.domain.dto.CatalogPageDto;
import com.literature.russian_literature.catalog.db.BookForCatalogMapper;
import com.literature.russian_literature.catalog.domain.dto.CatalogCategory;
import com.literature.russian_literature.catalog.domain.CatalogCategoryService;
import com.literature.russian_literature.catalog.domain.BookSelectionService;
import com.literature.russian_literature.security.SecurityUtils;

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
    private static final Logger LOG = LoggerFactory.getLogger(CatalogPageController.class);

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
        LOG.info("Called getCatalogPage");
        Long userId = SecurityUtils.getCurrentUserId();

        List<CatalogCategory> activeCategories = categoryService.getActiveCategories();

        List<CatalogCategoryWithBooksDto> categoriesWithBooks = activeCategories.stream()
                .map(category -> {
                    var books = bookSelectionService.getBooksForCategory(category, userId);
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
                false
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{code}/books")
    public ResponseEntity<Page<BookForCatalogDto>> getCategoryBooks(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        CatalogCategory category = categoryService.getCategoryByCode(code);
        Page<BookEntity> bookPage = bookSelectionService.getBooksForCategoryPage(category, pageable);
        Long userId = SecurityUtils.getCurrentUserId();
        Page<BookForCatalogDto> dtoPage = bookPage.map(book -> bookForCatalogMapper.toDto(book, userId));
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Новый endpoint для страницы категории с фильтрацией и сохранением сортировки
     */
    @GetMapping("/category/{code}/books/filter")
    public ResponseEntity<Page<BookForCatalogDto>> filterCategoryBooks(
            @PathVariable String code,
            @RequestParam(required = false) String genreIds,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String literature,
            @RequestParam(required = false) String readingType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        LOG.info("Called filterBooks with genreIds={}, grade={}, level={}, literature={}, readingType={}, " +
                        "page={}, size={}",
                genreIds, grade, level, literature, readingType, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookService.filterCategoryBooks(
                code, genreIds, grade, level, literature, readingType, pageable);
        Long userId = SecurityUtils.getCurrentUserId();
        Page<BookForCatalogDto> dtoPage = bookPage.map(book -> bookForCatalogMapper.toDto(book, userId));
        return ResponseEntity.ok(dtoPage);
    }
}
