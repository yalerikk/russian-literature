package com.literature.russian_literature.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class StringNormalizer {
    /**
     * Нормализует строку - обрезает пробелы
     */
    public String normalizeSpaces(String input) {
        return input == null ? null : input.trim();
    }

    /**
     * Нормализует строку - обрезает пробелы и заменяет "ё" на "е"
     */
    public String normalizeSpacesAndYo(String input) {
        if (input == null || input.isBlank()) return input;
        return replaceYo(input.trim());
    }

    /**
     * Нормализует ФИО - первая буква заглавная, остальные строчные и заменяет "ё" на "е"
     */
    public String normalizeName(String name) {
        if (name == null || name.isBlank()) return name;
        return capitalizeFirstLetter(replaceYo(name.trim()));
    }

    /**
     * Нормализует для поиска - приводит к нижнему регистру, заменяет "ё" на "е"
     */
    public String normalizeForSearch(String text) {
        if (text == null) return null;
        return replaceYo(text.trim()).toLowerCase();
    }

    /**
     * Нормализует URL - обрезает пробелы
     */
    public String normalizeUrl(String url) {
        return url == null ? null : url.trim();
    }

    /**
     * Нормализует дату - проверяет корректность
     */
    public LocalDate normalizeDate(String dateString) {
        if (dateString == null || dateString.isBlank()) return null;
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + dateString);
        }
    }

    /**
     * Форматирует дату для отображения
     */
    public String formatDate(LocalDate date) {
        if (date == null) return "н/д";
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    /**
     * Заменяет "ё" на "е"
     */
    public String replaceYo(String text) {
        return text
                .replace('ё', 'е')
                .replace('Ё', 'Е');
    }

    /**
     * Первая заглавная буква, остальные строчные
     */
    private String capitalizeFirstLetter(String text) {
        if (text.length() == 1) return text.toUpperCase();
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}