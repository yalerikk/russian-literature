package com.literature.russian_literature.tags.api;

import com.literature.russian_literature.genres.domain.Genre;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagMapper;
import com.literature.russian_literature.tags.domain.Tag;
import com.literature.russian_literature.tags.domain.TagService;
import com.literature.russian_literature.tags.domain.TagType;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private static final Logger LOG = LoggerFactory.getLogger(TagController.class);

    private final TagService tagService;
    private final TagMapper mapper;

    public TagController(TagService tagService, TagMapper mapper) {
        this.tagService = tagService;
        this.mapper = mapper;
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<Tag>> getTagsForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TagEntity> tagPage = tagService.getAllTagsForAdmin(pageable);
        Page<Tag> dtoPage = tagPage.map(mapper::toDomain);
        LOG.info("Admin list: page={}, size={}, total={}", page, size, dtoPage.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        LOG.info("Called getAllTags");
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<Tag>> getTagsByType(
            @RequestParam TagType type
    ) {
        LOG.info("Get tags by type={}", type);
        return ResponseEntity.ok(tagService.getTagsByType(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(
            @PathVariable Long id
    ) {
        LOG.info("Get tag by id={}", id);
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(
            @Valid @RequestBody Tag tagToCreate
    ) {
        Tag created = tagService.createTag(tagToCreate);
        LOG.info("Created tag with id={}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody Tag tagToUpdate
    ) {
        Tag updated = tagService.updateTag(id, tagToUpdate);
        LOG.info("Updated tag id={}", updated.id());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable Long id
    ) {
        tagService.deleteTag(id);
        LOG.info("Deleted tag id={}", id);
        return ResponseEntity.ok().build();
    }
}
