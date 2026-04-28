package com.literature.russian_literature.books.util;

import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.util.StringNormalizer;
import org.springframework.stereotype.Component;

@Component
public class BookNormalizer {
    private final StringNormalizer stringNormalizer;

    public BookNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public Book normalizeBook(Book book) {
        return new Book(
                book.id(),
                stringNormalizer.normalizeSpacesAndYo(book.title()),
                book.publicationYear(),
                stringNormalizer.replaceYo(book.description()),
                book.authorId(),
                stringNormalizer.normalizeSpaces(book.coverUrl()),
                book.createdAt(),
                book.updatedAt(),
                book.genreIds(),
                book.tagIds()
        );
    }
}
