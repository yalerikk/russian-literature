package com.literature.russian_literature.genres.util;

import com.literature.russian_literature.genres.domain.Genre;
import com.literature.russian_literature.util.StringNormalizer;
import org.springframework.stereotype.Component;

@Component
public class GenreNormalizer {
    private final StringNormalizer stringNormalizer;

    public GenreNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public Genre normalizeGenre(Genre genre) {
        return new Genre(
                genre.id(),
                normalizeName(genre.name())
        );
    }

    private String normalizeName(String name) {
        return stringNormalizer.normalizeName(name);
    }
}