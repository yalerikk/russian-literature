package com.literature.russian_literature.genres.db;

import com.literature.russian_literature.genres.domain.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public Genre toDomain(GenreEntity entity) {
        return new Genre(
                entity.getId(),
                entity.getName()
        );
    }

    public GenreEntity toEntity(Genre genre) {
        return new GenreEntity(
                genre.id(),
                genre.name()
        );
    }
}
