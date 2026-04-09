package com.literature.russian_literature.genres.api;

import com.literature.russian_literature.genres.domain.Genre;
import com.literature.russian_literature.genres.domain.GenreService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private static final Logger log = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;

    public GenreController(
            GenreService genreService
    ) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(
            @PathVariable Long id
    ) {
        log.info("Called getGenreById by id={}", id);
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        log.info("Called getAllGenres");
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Genre> getGenreByName(
            @PathVariable String name
    ) {
        log.info("Called getGenreByName by name={}", name);
        return ResponseEntity.ok(genreService.getGenreByName(name));
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(
            @Valid @RequestBody Genre genreToCreate
    ) {
        log.info("Called createGenre");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreService.createGenre(genreToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody Genre genreToUpdate
    ) {
        log.info("Called updateGenre id={}, genreToUpdate={}", id, genreToUpdate);
        Genre updated = genreService.updateGenre(id, genreToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.info("Called deleteGenre: id={}", id);
        genreService.deleteGenre(id);
        return ResponseEntity.ok().build();
    }
}