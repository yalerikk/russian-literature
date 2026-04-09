package com.literature.russian_literature.util;

import org.springframework.stereotype.Component;

@Component
public class GlobalValidator {
    public void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    public void validateFileUrl(String url) {
        validateNotBlank(url, "File URL");
        if (!url.matches("^(http|https)://.*\\.(pdf|epub|fb2|txt)$")) {
            throw new IllegalArgumentException("URL файла должен быть действительным и заканчиваться на .pdf, .epub, .fb2 или .txt");
        }
    }

    public void validatePhotoUrl(String url) {
        validateNotBlank(url, "URL изображения");
        if (!url.matches("^(http|https)://.*\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("URL изображения должен быть действительным и заканчиваться на .jpg, .jpeg или .png");
        }
    }
}
