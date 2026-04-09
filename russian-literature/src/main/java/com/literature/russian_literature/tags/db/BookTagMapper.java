package com.literature.russian_literature.tags.db;

import com.literature.russian_literature.tags.domain.BookTag;
import org.springframework.stereotype.Component;

@Component
public class BookTagMapper {
    public BookTag toDomain(BookTagEntity entity) {
        return new BookTag(
                entity.getId(),
                entity.getName(),
                entity.getType()
        );
    }

    public BookTagEntity toEntity(BookTag tag) {
        return new BookTagEntity(
                tag.id(),
                tag.name(),
                tag.type()
        );
    }
}