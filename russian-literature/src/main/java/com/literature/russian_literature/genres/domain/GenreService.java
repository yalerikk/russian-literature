package com.literature.russian_literature.genres.domain;

import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreMapper;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.genres.util.GenreNormalizer;
import com.literature.russian_literature.genres.util.GenreValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private static final Logger log = LoggerFactory.getLogger(GenreService.class);

    private final GenreRepository repository;
    private final GenreMapper mapper;
    private final GenreValidator validator;
    private final GenreNormalizer normalizer;
    private final BookRepository bookRepository;

    public GenreService(GenreRepository repository, GenreMapper mapper,
                        GenreValidator validator, GenreNormalizer normalizer, BookRepository bookRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.bookRepository = bookRepository;
    }

    public Genre getGenreById(Long id) {
        GenreEntity genreEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с id = " + id + " не найден"));
        return mapper.toDomain(genreEntity);
    }

    public List<Genre> getAllGenres() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Genre getGenreByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с названием '" + name + "' не найден"));
    }

    @Transactional
    public Genre createGenre(Genre genreToCreate) {
        // Нормализация -> Валидация
        Genre normalizedGenre = normalizer.normalizeGenre(genreToCreate);
        validator.validateForCreate(normalizedGenre);

        var entityToSave = mapper.toEntity(normalizedGenre);
        var savedEntity = repository.save(entityToSave);
        log.info("Создан жанр: '{}' с id = {}", savedEntity.getName(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Genre updateGenre(Long id, Genre genre) {
        GenreEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с id = " + id + " не найден"));

        // Нормализация -> Валидация
        Genre normalizedGenre = normalizer.normalizeGenre(genre);
        validator.validateForUpdate(id, normalizedGenre);

        existing.setName(normalizedGenre.name());
        GenreEntity updated = repository.save(existing);
        log.info("Обновлен жанр: '{}' с id = {}", updated.getName(), updated.getId());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteGenre(Long id) {
        GenreEntity genre = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с id = " + id + " не найден"));

        if (bookRepository.existsByGenres_Id(id)) {
            log.warn("Жанр '{}' используется в книгах, но будет удален вместе со связями", genre.getName());
            bookRepository.deleteAllGenreLinks(id);
        }

        repository.deleteById(id);
        log.info("Удален жанр: '{}' с id = {}", genre.getName(), id);
    }
}
