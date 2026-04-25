package com.literature.russian_literature.genres.api;

import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreMapper;
import com.literature.russian_literature.genres.domain.Genre;
import com.literature.russian_literature.genres.domain.GenreService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private static final Logger LOG = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;
    private final GenreMapper mapper;

    public GenreController(GenreService genreService, GenreMapper mapper) {
        this.genreService = genreService;
        this.mapper = mapper;
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<Genre>> getGenresForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GenreEntity> genrePage = genreService.getAllGenresForAdmin(pageable);
        Page<Genre> dtoPage = genrePage.map(mapper::toDomain);
        LOG.info("Admin list: page={}, size={}, total={}", page, size, dtoPage.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(
            @PathVariable Long id
    ) {
        LOG.info("Get genre by id={}", id);
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(
            @Valid @RequestBody Genre genreToCreate
    ) {
        Genre created = genreService.createGenre(genreToCreate);
        LOG.info("Created genre with id={}", created.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody Genre genreToUpdate
    ) {
        Genre updated = genreService.updateGenre(id, genreToUpdate);
        LOG.info("Updated genre id={}", updated.id());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(
            @PathVariable Long id
    ) {
        genreService.deleteGenre(id);
        LOG.info("Deleted genre id={}", id);
        return ResponseEntity.ok().build();
    }
}
