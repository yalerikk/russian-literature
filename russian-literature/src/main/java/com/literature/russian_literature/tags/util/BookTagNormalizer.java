package com.literature.russian_literature.tags.util;

import com.literature.russian_literature.tags.domain.BookTag;
import com.literature.russian_literature.util.StringNormalizer;
import org.springframework.stereotype.Component;

@Component
public class BookTagNormalizer {
    private final StringNormalizer stringNormalizer;

    public BookTagNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public BookTag normalizeBookTag(BookTag tag) {
        return new BookTag(
                tag.id(),
                normalizeName(tag.name()),
                tag.type()
        );
    }

    private String normalizeName(String name) {
        return stringNormalizer.normalizeName(name);
    }
}