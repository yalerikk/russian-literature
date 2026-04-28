package com.literature.russian_literature.catalog.domain.dto;

import java.util.List;

public record CatalogPageDto(
        List<CatalogCategoryWithBooksDto> categories,
        int totalCategories,
        boolean hasMore
) {

}
