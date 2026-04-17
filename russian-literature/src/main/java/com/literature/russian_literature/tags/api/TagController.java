package com.literature.russian_literature.tags.api;

import com.literature.russian_literature.tags.domain.Tag;
import com.literature.russian_literature.tags.domain.TagService;
import com.literature.russian_literature.tags.domain.TagType;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private static final Logger log = LoggerFactory.getLogger(TagController.class);

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(
            @PathVariable Long id
    ) {
        log.info("Called getTagById by id={}", id);
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        log.info("Called getAllTags");
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<Tag>> getTagsByType(
            @RequestParam TagType type
    ) {
        log.info("Called getTagsByType with type={}", type);
        return ResponseEntity.ok(tagService.getTagsByType(type));
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(
            @Valid @RequestBody Tag tagToCreate
    ) {
        log.info("Called createTag");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.createTag(tagToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody Tag tagToUpdate
    ) {
        log.info("Called updateTag id={}, tagToUpdate={}", id, tagToUpdate);
        Tag updated = tagService.updateTag(id, tagToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable Long id
    ) {
        log.info("Called deleteTag: id={}", id);
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }
}