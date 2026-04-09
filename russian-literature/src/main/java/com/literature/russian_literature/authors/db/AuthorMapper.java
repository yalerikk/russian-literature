package com.literature.russian_literature.authors.db;

import com.literature.russian_literature.authors.domain.Author;
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
                entity.getPhotoUrl()
        );
    }

    public AuthorEntity toEntity(Author author) {
        return new AuthorEntity(
                author.id(),
                author.firstName(),
                author.lastName(),
                author.middleName(),
                author.birthDate(),
                author.deathDate(),
                author.biography(),
                author.photoUrl()
        );
    }
}
