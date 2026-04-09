package com.literature.russian_literature.catalog.api.dto;

import java.util.List;

public record CatalogCategoryWithBooksDto(
        Long id,
        String name,
        String code,
        Integer displayOrder,
        Integer booksToShow,
        List<BookForCatalogDto> books
) {}
