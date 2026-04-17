package com.literature.russian_literature.util;

import com.literature.russian_literature.books.domain.BookFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class GlobalValidator {
    public void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    public void validateBookFileFormat(MultipartFile file, BookFormat format) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean valid = isFileValid(format, originalFilename, contentType);

        if (!valid) {
            throw new IllegalArgumentException("Файл не соответствует формату " + format +
                    ". Ожидаемый MIME-тип или расширение: " + format.name().toLowerCase());
        }
    }

    private static boolean isFileValid(BookFormat format, String originalFilename, String contentType) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }

        return switch (format) {
            case PDF -> "application/pdf".equals(contentType) || "pdf".equals(extension);
            case EPUB -> "application/epub+zip".equals(contentType) || "epub".equals(extension);
            case FB2 -> "application/fb2".equals(contentType) || "fb2".equals(extension) || "text/xml".equals(contentType);
            case TXT -> "text/plain".equals(contentType) || "txt".equals(extension);
        };
    }

    public void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения не может быть пустым");
        }

        String contentType = file.getContentType();
        boolean valid = isImageValid(file, contentType);

        if (!valid) {
            throw new IllegalArgumentException("Файл должен быть изображением в формате JPG, JPEG или PNG");
        }
    }

    private static boolean isImageValid(MultipartFile file, String contentType) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }

        return (contentType != null && (contentType.equals("image/pdf") || contentType.equals("image/jpeg") || contentType.equals("image/jpg") || contentType.equals("image/png"))) ||
                extension.equals("pdf") || extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");
    }

    public void validateFileUrl(String url) {
        validateNotBlank(url, "File URL");
        if (!url.matches("^(http|https)://.*\\.(pdf|epub|fb2|txt)$")) {
            throw new IllegalArgumentException("URL файла должен быть действительным и заканчиваться на .pdf, .epub, .fb2 или .txt");
        }
    }

    public void validatePhotoUrl(String url) {
        validateNotBlank(url, "URL изображения");
        if (!url.matches("^(http|https)://.*\\.(pdf|jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("URL изображения должен быть действительным и заканчиваться на .pdf, .jpg, .jpeg или .png");
        }
    }
}
