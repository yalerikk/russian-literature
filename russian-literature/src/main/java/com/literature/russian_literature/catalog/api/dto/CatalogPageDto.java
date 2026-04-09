package com.literature.russian_literature.catalog.api.dto;

import java.util.List;

public record CatalogPageDto(
        List<CatalogCategoryWithBooksDto> categories,
        int totalCategories,
        boolean hasMore
) {}
