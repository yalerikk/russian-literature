package com.literature.russian_literature.catalog.api;

import com.literature.russian_literature.catalog.domain.CatalogCategory;
import com.literature.russian_literature.catalog.domain.CatalogCategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalog/categories")
public class CatalogCategoryController {
    private static final Logger log = LoggerFactory.getLogger(CatalogCategoryController.class);

    private final CatalogCategoryService categoryService;

    public CatalogCategoryController(CatalogCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogCategory>> getAllCategories() {
        log.info("Called getAllCategories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CatalogCategory>> getActiveCategories() {
        log.info("Called getActiveCategories");
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogCategory> getCategoryById(@PathVariable Long id) {
        log.info("Called getCategoryById by id={}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CatalogCategory> getCategoryByCode(@PathVariable String code) {
        log.info("Called getCategoryByCode by code={}", code);
        return ResponseEntity.ok(categoryService.getCategoryByCode(code));
    }

    @PostMapping
    public ResponseEntity<CatalogCategory> createCategory(@Valid @RequestBody CatalogCategory category) {
        log.info("Called createCategory");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogCategory> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CatalogCategory category) {
        log.info("Called updateCategory id={}", id);
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Called deleteCategory id={}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reorder")
    public ResponseEntity<Void> reorderCategories(@RequestBody List<Long> categoryIdsInOrder) {
        log.info("Called reorderCategories");
        categoryService.reorderCategories(categoryIdsInOrder);
        return ResponseEntity.ok().build();
    }
}
