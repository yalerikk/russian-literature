package com.literature.russian_literature.userbooks.domain;

public record UserBookStatusResponse(
        boolean favorite,
        BookStatus status
) {

}
