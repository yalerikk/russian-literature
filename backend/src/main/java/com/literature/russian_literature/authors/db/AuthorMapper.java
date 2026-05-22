package com.literature.russian_literature.authors.db;

import com.literature.russian_literature.authors.domain.dto.Author;

import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public Author toDomain(AuthorEntity entity) {
        return new Author(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getMiddleName(),
                entity.getBirthDate(),
                entity.getDeathDate(),
                entity.getBiography(),
                entity.getPhotoUrl(),
                entity.getBookCount()   // int -> Integer
        );
    }

    public AuthorEntity toEntity(Author author) {
        AuthorEntity entity = new AuthorEntity();
        entity.setId(author.id());
        entity.setFirstName(author.firstName());
        entity.setLastName(author.lastName());
        entity.setMiddleName(author.middleName());
        entity.setBirthDate(author.birthDate());
        entity.setDeathDate(author.deathDate());
        entity.setBiography(author.biography());
        entity.setPhotoUrl(author.photoUrl());
        return entity;
    }
}
