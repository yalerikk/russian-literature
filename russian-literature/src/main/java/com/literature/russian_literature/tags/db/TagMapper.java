package com.literature.russian_literature.tags.db;

import com.literature.russian_literature.tags.domain.Tag;

import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public Tag toDomain(TagEntity entity) {
        return new Tag(
                entity.getId(),
                entity.getName(),
                entity.getType()
        );
    }

    public TagEntity toEntity(Tag tag) {
        return new TagEntity(
                tag.id(),
                tag.name(),
                tag.type()
        );
    }
}
