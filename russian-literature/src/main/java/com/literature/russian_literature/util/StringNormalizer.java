package com.literature.russian_literature.util;

import org.springframework.stereotype.Component;

@Component
public class StringNormalizer {
    public String normalizeSpaces(String input) {
        return input == null ? null : input.trim();
    }

    // Нормализует строку - обрезает пробелы и заменяет "ё" на "е"
    public String normalizeSpacesAndYo(String input) {
        if (input == null || input.isBlank()) return input;
        return replaceYo(input.trim());
    }

    // Нормализует ФИО - первая буква заглавная, остальные строчные и заменяет "ё" на "е"
    public String normalizeName(String name) {
        if (name == null || name.isBlank()) return name;
        return capitalizeFirstLetter(replaceYo(name.trim()));
    }

    // Заменяет "ё" на "е"
    public String replaceYo(String text) {
        return text
                .replace('ё', 'е')
                .replace('Ё', 'Е');
    }

    private String capitalizeFirstLetter(String text) {
        if (text.length() == 1) return text.toUpperCase();
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
