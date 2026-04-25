package com.literature.russian_literature.catalog.api;

import com.literature.russian_literature.catalog.domain.dto.CreateCustomCategoryDto;
import com.literature.russian_literature.catalog.domain.dto.UpdateCustomCategoryDto;
import com.literature.russian_literature.catalog.domain.dto.CatalogCategory;
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
    private static final Logger LOG = LoggerFactory.getLogger(CatalogCategoryController.class);

    private final CatalogCategoryService categoryService;

    public CatalogCategoryController(CatalogCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogCategory>> getAllCategories(
            @RequestParam(required = false) Boolean isActive
    ) {
        LOG.info("Called getAllCategories with isActive={}", isActive);
        return ResponseEntity.ok(categoryService.getCategoriesFiltered(isActive));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogCategory> getCategoryById(
            @PathVariable Long id
    ) {
        LOG.info("Called getCategoryById by id={}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CatalogCategory> getCategoryByCode(
            @PathVariable String code
    ) {
        LOG.info("Called getCategoryByCode by code={}", code);
        return ResponseEntity.ok(categoryService.getCategoryByCode(code));
    }

    @PostMapping
    public ResponseEntity<CatalogCategory> createCustomCategory(
            @Valid @RequestBody CreateCustomCategoryDto request
    ) {
        LOG.info("Called createCategory");
        CatalogCategory created = categoryService.createCustomCategory(request.tagIds());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogCategory> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomCategoryDto request
    ) {
        LOG.info("Called updateCategory id={}", id);
        CatalogCategory updated = categoryService.updateCategory(id, request.name(), request.isActive());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        LOG.info("Called deleteCategory id={}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
