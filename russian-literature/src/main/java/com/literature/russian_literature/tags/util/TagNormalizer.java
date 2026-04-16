package com.literature.russian_literature.tags.util;

import com.literature.russian_literature.tags.domain.Tag;
import com.literature.russian_literature.util.StringNormalizer;
import org.springframework.stereotype.Component;

@Component
public class TagNormalizer {
    private final StringNormalizer stringNormalizer;

    public TagNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public Tag normalizeTag(Tag tag) {
        return new Tag(
                tag.id(),
                normalizeName(tag.name()),
                tag.type()
        );
    }

    private String normalizeName(String name) {
        return stringNormalizer.normalizeName(name);
    }
}