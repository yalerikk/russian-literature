package com.literature.russian_literature.catalog.db;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.catalog.api.dto.BookForCatalogDto;
import org.springframework.stereotype.Component;

@Component
public class BookForCatalogMapper {
    public BookForCatalogDto toDto(BookEntity book) {
        String authorFullName = "";
        String authorShortName = "";
        if (book.getAuthor() != null) {
            authorFullName = book.getAuthor().getFullName();
            authorShortName = book.getAuthor().getShortName();
        }
        return new BookForCatalogDto(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getAuthor() != null ? book.getAuthor().getId() : null,
                authorFullName,
                authorShortName,
                book.getCoverUrl(),
                book.getCreatedAt(),
                null, // рейтинг
                null  // кол-во оценок
        );
    }
}
