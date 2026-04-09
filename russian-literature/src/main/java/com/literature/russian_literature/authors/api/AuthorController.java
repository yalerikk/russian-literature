package com.literature.russian_literature.authors.api;

import com.literature.russian_literature.authors.domain.Author;
import com.literature.russian_literature.authors.domain.AuthorService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @PathVariable("id") Long id
    ) {
        log.info("Called getAuthorById by id={}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(authorService.getAuthorById(id));
    }

    // GET ALL
    @GetMapping()
    public ResponseEntity<List<Author>> getAllAuthors() {
        log.info("Called getAllAuthors");
        return ResponseEntity.status(HttpStatus.OK)
                .body(authorService.getAllAuthors());
    }

    // POST
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Valid @RequestBody Author authorToCreate
    ) {
        log.info("Called createAuthor");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createAuthor(authorToCreate));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody Author authorToUpdate
    ) {
        log.info("Called updateAuthor id={}, authorToUpdate={}", id, authorToUpdate);
        Author updated = authorService.updateAuthor(id, authorToUpdate);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id
    ) {
        log.info("Called deleteAuthor: id={}", id);
        authorService.deleteAuthor(id);
        return ResponseEntity.ok()
                .build();
    }

    /*
    // Получить всех авторов для выпадающего списка
    @GetMapping("/authors/for-select")
    public List<AuthorForSelect> getAuthorsForSelect() {
        return authorService.getAuthorsForSelect();
    }

    // Поиск авторов по части ФИО (для автодополнения)
    @GetMapping("/authors/search")
    public List<AuthorForSelect> searchAuthors(
            @RequestParam String query
    ) {
        return authorService.searchAuthors(query);
    }

    // Получить автора с книгами (для страницы автора)
    @GetMapping("/authors/{id}/with-books")
    public AuthorWithBooksResponse getAuthorWithBooks(
            @PathVariable Long id
    ) {
        return authorService.getAuthorWithBooks(id);
    }
     */
}
