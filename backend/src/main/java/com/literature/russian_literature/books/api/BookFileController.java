package com.literature.russian_literature.books.api;

import com.literature.russian_literature.books.domain.BookFormat;
import com.literature.russian_literature.books.domain.BookService;
import com.literature.russian_literature.books.domain.dto.BookFileResponse;
import com.literature.russian_literature.cloudinary.CloudinaryService;
import com.literature.russian_literature.util.GlobalValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books/{bookId}/files")
public class BookFileController {
    private static final Logger LOG = LoggerFactory.getLogger(BookFileController.class);

    private final BookService bookService;
    private final CloudinaryService cloudinaryService;
    private final GlobalValidator globalValidator;

    public BookFileController(BookService bookService, CloudinaryService cloudinaryService,
                              GlobalValidator globalValidator) {
        this.bookService = bookService;
        this.cloudinaryService = cloudinaryService;
        this.globalValidator = globalValidator;
    }

    @GetMapping
    public ResponseEntity<List<BookFileResponse>> getFiles(
            @PathVariable Long bookId
    ) {
        LOG.info("Fetching files for book id={}", bookId);
        List<BookFileResponse> files = bookService.getFilesByBookId(bookId);
        LOG.info("Found {} files for book id={}", files.size(), bookId);
        return ResponseEntity.ok(files);
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") BookFormat format
    ) {
        try {
            globalValidator.validateBookFileFormat(file, format);

            String url = cloudinaryService.uploadFile(file, "books");
            String publicId = CloudinaryService.extractPublicIdFromUrl(url);

            BookFileResponse response = bookService.addFileToBook(bookId, url, format, publicId);
            LOG.info("Uploaded {} file for book id={}", format, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            LOG.error("Failed to upload file for book id={}: {}", bookId, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long bookId,
            @PathVariable Long fileId
    ) {
        bookService.deleteFileFromBook(bookId, fileId);
        LOG.info("Deleted file id={} from book id={}", fileId, bookId);
        return ResponseEntity.noContent().build();
    }
}
