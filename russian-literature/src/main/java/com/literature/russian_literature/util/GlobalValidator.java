package com.literature.russian_literature.util;

import com.literature.russian_literature.books.domain.BookFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class GlobalValidator {
    public void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }

    public void validateBookFileFormat(MultipartFile file, BookFormat format) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean valid = isFileValid(format, originalFilename, contentType);

        if (!valid) {
            throw new IllegalArgumentException("File does not match format " + format +
                    ". Expected MIME type or extension: " + format.name().toLowerCase());
        }
    }

    private static boolean isFileValid(BookFormat format, String originalFilename, String contentType) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }

        return switch (format) {
            case PDF -> "application/pdf".equals(contentType) || "application/octet-stream".equals(contentType) || "pdf".equals(extension);
            case EPUB -> "application/epub+zip".equals(contentType) || "epub".equals(extension);
            case FB2 -> "application/fb2".equals(contentType) || "fb2".equals(extension) || "text/xml".equals(contentType);
            case TXT -> "text/plain".equals(contentType) || "txt".equals(extension);
        };
    }

    public void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        String contentType = file.getContentType();
        boolean valid = isImageValid(file, contentType);

        if (!valid) {
            throw new IllegalArgumentException("File must be an image in JPG, JPEG, or PNG format");
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

    public void validatePhotoUrl(String url) {
        validateNotBlank(url, "Image URL");
        if (!url.matches("^(http|https)://.*\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("Image URL must be valid and end with .jpg, .jpeg, or .png");
        }
    }
}
