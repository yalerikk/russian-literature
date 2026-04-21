package com.literature.russian_literature.catalog.api;

import com.literature.russian_literature.catalog.api.dto.CreateCustomCategoryDto;
import com.literature.russian_literature.catalog.api.dto.UpdateCustomCategoryDto;
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

    // GET ALL - FOR ADMIN with filter by active status
    @GetMapping
    public ResponseEntity<List<CatalogCategory>> getAllCategories(
            @RequestParam(required = false) Boolean isActive
    ) {
        log.info("Called getAllCategories with isActive={}", isActive);
        return ResponseEntity.ok(categoryService.getCategoriesFiltered(isActive));
    }

    // GET BY ID - FOR ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<CatalogCategory> getCategoryById(@PathVariable Long id) {
        log.info("Called getCategoryById by id={}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // GET BY CODE - FOR ALL
    @GetMapping("/code/{code}")
    public ResponseEntity<CatalogCategory> getCategoryByCode(@PathVariable String code) {
        log.info("Called getCategoryByCode by code={}", code);
        return ResponseEntity.ok(categoryService.getCategoryByCode(code));
    }

    @PostMapping
    public ResponseEntity<CatalogCategory> createCustomCategory(@Valid @RequestBody CreateCustomCategoryDto request) {
        log.info("Called createCategory");
        CatalogCategory created = categoryService.createCustomCategory(request.tagIds());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogCategory> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomCategoryDto request
    ) {
        log.info("Called updateCategory id={}", id);
        CatalogCategory updated = categoryService.updateCategory(id, request.name(), request.isActive());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Called deleteCategory id={}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
