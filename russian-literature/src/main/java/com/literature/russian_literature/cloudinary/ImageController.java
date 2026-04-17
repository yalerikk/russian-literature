package com.literature.russian_literature.cloudinary;

import com.literature.russian_literature.books.api.BookController;
import com.literature.russian_literature.util.GlobalValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final CloudinaryService cloudinaryService;
    private final GlobalValidator globalValidator;

    public ImageController(CloudinaryService cloudinaryService, GlobalValidator globalValidator) {
        this.cloudinaryService = cloudinaryService;
        this.globalValidator = globalValidator;
    }

    @PostMapping("/upload/book-cover")
    public ResponseEntity<?> uploadBookCover(@RequestParam("file") MultipartFile file) {
        try {
            globalValidator.validateImageFile(file);
            String url = cloudinaryService.uploadFile(file, "book-covers");
            log.info("Called uploadBookCover");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/upload/author-photo")
    public ResponseEntity<?> uploadAuthorPhoto(@RequestParam("file") MultipartFile file) {
        try {
            globalValidator.validateImageFile(file);
            String url = cloudinaryService.uploadFile(file, "author-photos");
            log.info("Called uploadAuthorPhoto");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
