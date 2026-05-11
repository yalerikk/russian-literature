package com.literature.russian_literature.books.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BookDetailDto(
        Long id,
        String title,
        Integer publicationYear,
        String description,
        String coverUrl,
        AuthorInfo author,
        List<GenreInfo> genres,
        List<TagInfo> tags,
        Double rating,
        Integer ratingCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record AuthorInfo(Long id, String shortName) {}
    public record GenreInfo(Long id, String name) {}
    public record TagInfo(Long id, String name, String type) {}
}
