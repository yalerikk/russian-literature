package com.literature.russian_literature.search.domain;

public record SearchSuggestion (
    String type,      // "BOOK" или "AUTHOR"
    Long id,
    String title,     // для книги – название, для автора – ФИО
    String subtitle,  // для книги – автор, для автора – годы жизни
    String imageUrl
) {

}
