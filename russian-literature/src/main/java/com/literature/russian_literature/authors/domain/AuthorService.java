package com.literature.russian_literature.authors.domain;

import com.literature.russian_literature.authors.db.AuthorMapper;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.domain.dto.Author;
import com.literature.russian_literature.authors.domain.dto.AuthorForSelect;
import com.literature.russian_literature.authors.util.AuthorNormalizer;
import com.literature.russian_literature.authors.util.AuthorValidator;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.cloudinary.CloudinaryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository repository;
    private final AuthorMapper mapper;
    private final AuthorValidator validator;
    private final AuthorNormalizer normalizer;
    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public AuthorService(AuthorRepository repository, AuthorMapper mapper,
                         AuthorValidator validator, AuthorNormalizer normalizer,
                         BookRepository bookRepository, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.normalizer = normalizer;
        this.bookRepository = bookRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Author getAuthorById(Long id) {
        AuthorEntity authorEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with id = " + id + " not found"
                ));

        return mapper.toDomain(authorEntity);
    }

    public List<Author> getAllAuthors() {
        List<AuthorEntity> allAuthors = repository.findAll();

        return allAuthors
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Page<AuthorEntity> getAllAuthorsForAdmin(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<AuthorForSelect> getAuthorsForSelect() {
        return repository.findAllForSelect();
    }

    @Transactional
    public Author createAuthor(Author authorToCreate) {
        Author normalizedAuthor = normalizer.normalizeAuthor(authorToCreate);
        validator.validateCreate(normalizedAuthor);

        var entityToSave = mapper.toEntity(normalizedAuthor);
        var savedEntity = repository.save(entityToSave);
        LOG.info("Created author: '{}' with id = {}", savedEntity.getFullName(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Transactional
    public Author updateAuthor(Long id, Author author) {
        AuthorEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id = " + id + " not found"));

        validator.validateUpdate(id, author);
        Author normalizedAuthor = normalizer.normalizeAuthor(author);

        existing.setFirstName(normalizedAuthor.firstName());
        existing.setLastName(normalizedAuthor.lastName());
        existing.setMiddleName(normalizedAuthor.middleName());
        existing.setBirthDate(normalizedAuthor.birthDate());
        existing.setDeathDate(normalizedAuthor.deathDate());
        existing.setBiography(normalizedAuthor.biography());
        existing.setPhotoUrl(normalizedAuthor.photoUrl());

        AuthorEntity updated = repository.save(existing);
        LOG.info("Updated author: '{}' with id = {}", updated.getFullName(), updated.getId());
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        var author = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id = " + id + " not found"));

        if (bookRepository.existsByAuthorId(id)) {
            throw new IllegalStateException("Unable to delete author '" + author.getFullName() +
                    "', because he(she) has books. Delete or reassign the books first.");
        }

        try {
            if (author.getPhotoUrl() != null && !author.getPhotoUrl().isBlank()) {
                String publicId = CloudinaryService.extractPublicIdFromUrl(author.getPhotoUrl());
                cloudinaryService.deleteFile(publicId, "image");
            }
        } catch (Exception e) {
            LOG.error("Error deleting author's photo from Cloudinary: {}", e.getMessage());
        }

        repository.deleteById(id);
        LOG.info("Deleted author: '{}' with id = {}", author.getFullName(), id);
    }
}
