package com.literature.russian_literature.books.domain.dto;

import com.literature.russian_literature.books.domain.BookFormat;

public record BookFileResponse(
        Long id,
        String fileUrl,
        BookFormat format,
        String publicId
) {

}
