package com.literature.russian_literature.tags.api;

import com.literature.russian_literature.tags.domain.BookTag;
import com.literature.russian_literature.tags.domain.BookTagService;
import com.literature.russian_literature.tags.domain.TagType;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-tags")
public class BookTagController {
    private static final Logger log = LoggerFactory.getLogger(BookTagController.class);

    private final BookTagService bookTagService;

    public BookTagController(BookTagService bookTagService) {
        this.bookTagService = bookTagService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookTag> getBookTagById(
            @PathVariable Long id
    ) {
        log.info("Called getBookTagById by id={}", id);
        return ResponseEntity.ok(bookTagService.getBookTagById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookTag>> getAllBookTags() {
        log.info("Called getAllBookTags");
        return ResponseEntity.ok(bookTagService.getAllBookTags());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BookTag> getBookTagByName(
            @PathVariable String name
    ) {
        log.info("Called getBookTagByName by name={}", name);
        return ResponseEntity.ok(bookTagService.getBookTagByName(name));
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<BookTag>> getBookTagsByType(
            @RequestParam TagType type
    ) {
        log.info("Called getBookTagsByType with type={}", type);
        return ResponseEntity.ok(bookTagService.getBookTagsByType(type));
    }

    @PostMapping
    public ResponseEntity<BookTag> createBookTag(
            @Valid @RequestBody BookTag tagToCreate
    ) {
        log.info("Called createBookTag");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookTagService.createBookTag(tagToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookTag> updateBookTag(
            @PathVariable Long id,
            @Valid @RequestBody BookTag tagToUpdate
    ) {
        log.info("Called updateBookTag id={}, tagToUpdate={}", id, tagToUpdate);
        BookTag updated = bookTagService.updateBookTag(id, tagToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookTag(
            @PathVariable Long id
    ) {
        log.info("Called deleteBookTag: id={}", id);
        bookTagService.deleteBookTag(id);
        return ResponseEntity.ok().build();
    }
}