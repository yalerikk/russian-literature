package com.literature.russian_literature.userbooks.domain;

import java.time.LocalDateTime;

public record UserBook(
        Long id,
        Long bookId,
        String bookTitle,
        String coverUrl,
        String authorShortName,
        BookStatus status,
        Integer progress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
