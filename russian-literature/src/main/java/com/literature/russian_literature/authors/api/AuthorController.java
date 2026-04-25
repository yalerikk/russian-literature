package com.literature.russian_literature.authors.api;

import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorMapper;
import com.literature.russian_literature.authors.domain.dto.Author;
import com.literature.russian_literature.authors.domain.AuthorService;
import com.literature.russian_literature.authors.domain.dto.AuthorForSelect;

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
@RequestMapping("/authors")
public class AuthorController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;
    private final AuthorMapper mapper;

    public AuthorController(AuthorService authorService, AuthorMapper mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @PathVariable("id") Long id
    ) {
        LOG.info("Called getAuthorById by id={}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(authorService.getAuthorById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Author>> getAllAuthors() {
        LOG.info("Called getAllAuthors");
        return ResponseEntity.status(HttpStatus.OK)
                .body(authorService.getAllAuthors());
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<Author>> getAuthorsForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuthorEntity> authorPage = authorService.getAllAuthorsForAdmin(pageable);
        Page<Author> dtoPage = authorPage.map(mapper::toDomain);
        LOG.info("Admin list: page={}, size={}, total={}", page, size, dtoPage.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/for-select")
    public List<AuthorForSelect> getAuthorsForSelect() {
        LOG.info("Called getAuthorsForSelect");
        return authorService.getAuthorsForSelect();
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Valid @RequestBody Author authorToCreate
    ) {
        LOG.info("Called createAuthor");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createAuthor(authorToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody Author authorToUpdate
    ) {
        LOG.info("Called updateAuthor id={}, authorToUpdate={}", id, authorToUpdate);
        Author updated = authorService.updateAuthor(id, authorToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id
    ) {
        LOG.info("Called deleteAuthor id={}", id);
        authorService.deleteAuthor(id);
        return ResponseEntity.ok()
                .build();
    }
}
