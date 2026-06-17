package com.literature.russian_literature.books.util;

import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.books.domain.dto.Book;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.tags.db.TagRepository;
import com.literature.russian_literature.tags.domain.TagType;
import com.literature.russian_literature.util.GlobalValidator;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BookValidator {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final GlobalValidator globalValidator;

    public BookValidator(BookRepository bookRepository, AuthorRepository authorRepository,
                         GenreRepository genreRepository, TagRepository tagRepository,
                         GlobalValidator globalValidator) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.globalValidator = globalValidator;
    }

    public void validateForCreate(Book book) {
        validateRequiredFields(book);
        validateAuthorExists(book.authorId());
        validateTitleUniqueness(book.title(), book.authorId());
        validatePublicationYear(book.publicationYear());
        validateGenres(book.genreIds());
        validateTags(book.tagIds());
    }

    public void validateForUpdate(Long id, Book book) {
        validateRequiredFields(book);
        validateAuthorExists(book.authorId());
        validateTitleUniquenessOnUpdate(id, book.title(), book.authorId());
        validatePublicationYear(book.publicationYear());
        validateGenres(book.genreIds());
        validateTags(book.tagIds());
    }

    private void validateRequiredFields(Book book) {
        globalValidator.validateNotBlank(book.title(), "Title");
        globalValidator.validateNotBlank(book.description(), "Description");
        validateGenresNotEmpty(book.genreIds());
        validateTagsNotEmpty(book.tagIds());
    }

    private void validateAuthorExists(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new IllegalArgumentException("Author with id = " + authorId + " does not exist");
        }
    }

    private void validateTitleUniqueness(String title, Long authorId) {
        if (bookRepository.existsByTitleAndAuthorId(title, authorId)) {
            throw new IllegalArgumentException("Book with title '" + title + "' already exists for this author");
        }
    }

    private void validateTitleUniquenessOnUpdate(Long id, String title, Long authorId) {
        if (bookRepository.existsByTitleAndAuthorIdExcludingId(title, authorId, id)) {
            throw new IllegalArgumentException("Book with title '" + title + "' already exists for this author");
        }
    }

    private void validatePublicationYear(Integer year) {
        if (year < 1500) {
            throw new IllegalArgumentException("Publication year cannot be earlier than 1500");
        }
        if (year > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Publication year cannot be in the future");
        }
    }

    private void validateGenresNotEmpty(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one genre");
        }
    }

    private void validateTagsNotEmpty(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one tag");
        }
    }

    private void validateGenres(Set<Long> genreIds) {
        for (Long genreId : genreIds) {
            if (!genreRepository.existsById(genreId)) {
                var genre = genreRepository.findById(genreId);
                String genreName = genre.map(g -> " '" + g.getName() + "'").orElse("");
                throw new IllegalArgumentException("Genre with id = " + genreId + genreName + " does not exist. Please select a genre from the list.");
            }
        }
    }

    private void validateTags(Set<Long> tagIds) {
        for (Long tagId : tagIds) {
            if (!tagRepository.existsById(tagId)) {
                var tag = tagRepository.findById(tagId);
                String tagName = tag.map(t -> " '" + t.getName() + "'").orElse("");
                throw new IllegalArgumentException("Tag with id = " + tagId + tagName + " does not exist. Please select a tag from the list.");
            }
        }

        var tags = tagRepository.findAllById(tagIds);

        boolean hasGrade = tags.stream().anyMatch(tag -> tag.getType() == TagType.GRADE);
        boolean hasLevel = tags.stream().anyMatch(tag -> tag.getType() == TagType.LEVEL);
        boolean hasCategory = tags.stream().anyMatch(tag -> tag.getType() == TagType.CATEGORY);
        boolean hasReadingType = tags.stream().anyMatch(tag -> tag.getType() == TagType.READING_TYPE);

        if (!hasGrade) {
            throw new IllegalArgumentException("Book must have a grade tag (10th or 11th grade)");
        }

        if (!hasLevel) {
            throw new IllegalArgumentException("Book must have a level tag (Base or Advanced)");
        }

        if (!hasCategory) {
            throw new IllegalArgumentException("Book must have a category tag (Russian or Foreign literature)");
        }

        if (!hasReadingType) {
            throw new IllegalArgumentException("Book must have a reading type tag (Main, Summer, or Additional)");
        }

        long gradeCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.GRADE)
                .count();
        if (gradeCount > 1) {
            throw new IllegalArgumentException("Book can belong to only one grade");
        }

        long levelCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.LEVEL)
                .count();
        if (levelCount > 1) {
            throw new IllegalArgumentException("Book can have only one level");
        }

        long categoryCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.CATEGORY)
                .count();
        if (categoryCount > 1) {
            throw new IllegalArgumentException("Book can belong to only one category");
        }

        long readingTypeCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.READING_TYPE)
                .count();
        if (readingTypeCount > 1) {
            throw new IllegalArgumentException("Book can have only one reading type tag");
        }
    }
}
