package com.literature.russian_literature.books.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.tags.domain.TagType;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookSpecifications {
    public static Specification<BookEntity> byGenres(List<Long> genreIds) {
        return (root, query, cb) -> {
            if (genreIds == null || genreIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Object, Object> genresJoin = root.join("genres", JoinType.LEFT);
            return genresJoin.get("id").in(genreIds);
        };
    }

    public static Specification<BookEntity> byTagTypeAndName(TagType type, String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            Join<Object, Object> tagsJoin = root.join("tags", JoinType.LEFT);
            return cb.and(
                    cb.equal(tagsJoin.get("type"), type),
                    cb.equal(tagsJoin.get("name"), name)
            );
        };
    }
}
