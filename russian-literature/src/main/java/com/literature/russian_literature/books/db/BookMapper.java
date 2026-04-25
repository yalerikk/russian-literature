package com.literature.russian_literature.books.db;

import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.tags.db.TagEntity;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    public Book toDomain(BookEntity entity) {
        Long authorId = entity.getAuthor() != null ? entity.getAuthor().getId() : null;

        Set<Long> genreIds = entity.getGenres().stream()
                .map(GenreEntity::getId)
                .collect(Collectors.toSet());

        Set<Long> tagIds = entity.getTags().stream()
                .map(TagEntity::getId)
                .collect(Collectors.toSet());

        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getPublicationYear(),
                entity.getDescription(),
                authorId,
                entity.getCoverUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                genreIds,
                tagIds
        );
    }

    public BookEntity toEntity(Book book, AuthorEntity author, Set<GenreEntity> genres, Set<TagEntity> tags) {
        var entity = new BookEntity();
        entity.setId(book.id());
        entity.setTitle(book.title());
        entity.setPublicationYear(book.publicationYear());
        entity.setDescription(book.description());
        entity.setAuthor(author);
        entity.setCoverUrl(book.coverUrl());
        entity.setCreatedAt(book.createdAt());
        entity.setUpdatedAt(book.updatedAt());
        entity.setGenres(genres);
        entity.setTags(tags);

        return entity;
    }
}
