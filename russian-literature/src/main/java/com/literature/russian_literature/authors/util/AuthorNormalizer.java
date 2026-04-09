package com.literature.russian_literature.authors.util;

import com.literature.russian_literature.authors.domain.Author;
import com.literature.russian_literature.util.StringNormalizer;
import org.springframework.stereotype.Component;

@Component
public class AuthorNormalizer {
    private final StringNormalizer stringNormalizer;

    public AuthorNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public Author normalizeAuthor(Author author) {
        return new Author(
                author.id(),
                stringNormalizer.normalizeName(author.firstName()),
                stringNormalizer.normalizeName(author.lastName()),
                stringNormalizer.normalizeName(author.middleName()),
                author.birthDate(),
                author.deathDate(),
                stringNormalizer.replaceYo(author.biography()),
                stringNormalizer.normalizeUrl(author.photoUrl())
        );
    }

    /**
     * Нормализует данные для поиска - для использования в репозитории
     */
    public String normalizeForSearch(String text) {
        return stringNormalizer.normalizeForSearch(text);
    }
}
