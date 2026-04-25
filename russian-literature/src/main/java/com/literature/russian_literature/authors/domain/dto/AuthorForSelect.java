package com.literature.russian_literature.authors.domain.dto;

public record AuthorForSelect(
        Long id,
        String lastName,
        String firstName,
        String middleName
) {

}
