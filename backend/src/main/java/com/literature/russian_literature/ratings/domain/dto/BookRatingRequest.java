package com.literature.russian_literature.ratings.domain.dto;

public record BookRatingRequest(
        Long bookId,
        Integer rating
) {

}
