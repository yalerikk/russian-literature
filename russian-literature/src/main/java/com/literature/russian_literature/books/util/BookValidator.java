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
        globalValidator.validateNotBlank(book.title(), "Название");
        globalValidator.validateNotBlank(book.description(), "Описание");

        // Жанры и теги проверяем отдельно
        validateGenresNotEmpty(book.genreIds());
        validateTagsNotEmpty(book.tagIds());
    }

    private void validateAuthorExists(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new IllegalArgumentException("Автора с id = " + authorId + " не существует");
        }
    }

    private void validateTitleUniqueness(String title, Long authorId) {
        if (bookRepository.existsByTitleAndAuthorId(title, authorId)) {
            throw new IllegalArgumentException("Книга с названием '" + title + "' уже существует у этого автора");
        }
    }

    private void validateTitleUniquenessOnUpdate(Long id, String title, Long authorId) {
        if (bookRepository.existsByTitleAndAuthorIdExcludingId(title, authorId, id)) {
            throw new IllegalArgumentException("Книга с названием '" + title + "' уже существует у этого автора");
        }
    }

    private void validatePublicationYear(Integer year) {
        if (year < 1500) {
            throw new IllegalArgumentException("Год публикации не может быть ранее 1500 года");
        }
        if (year > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Год публикации не может быть в будущем");
        }
    }

    private void validateGenresNotEmpty(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            throw new IllegalArgumentException("Книга должна иметь хотя бы один жанр");
        }
    }

    private void validateTagsNotEmpty(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            throw new IllegalArgumentException("Книга должна иметь хотя бы один тег");
        }
    }

    private void validateGenres(Set<Long> genreIds) {
        // Проверяем, что все ID жанров существуют
        for (Long genreId : genreIds) {
            if (!genreRepository.existsById(genreId)) {
                // Более понятное сообщение для администратора
                var genre = genreRepository.findById(genreId);
                String genreName = genre.map(g -> " '" + g.getName() + "'").orElse("");
                throw new IllegalArgumentException("Жанр с id = " + genreId + genreName + " не существует. Пожалуйста, выберите жанр из списка.");
            }
        }
    }

    private void validateTags(Set<Long> tagIds) {
        // Проверяем существование всех тегов
        for (Long tagId : tagIds) {
            if (!tagRepository.existsById(tagId)) {
                // Более понятное сообщение для администратора
                var tag = tagRepository.findById(tagId);
                String tagName = tag.map(t -> " '" + t.getName() + "'").orElse("");
                throw new IllegalArgumentException("Тег с id = " + tagId + tagName + " не существует. Пожалуйста, выберите тег из списка.");
            }
        }

        // Получаем все теги для проверки обязательных типов
        var tags = tagRepository.findAllById(tagIds);

        // Проверяем обязательные типы тегов
        boolean hasGrade = tags.stream().anyMatch(tag -> tag.getType() == TagType.GRADE);
        boolean hasLevel = tags.stream().anyMatch(tag -> tag.getType() == TagType.LEVEL);
        boolean hasCategory = tags.stream().anyMatch(tag -> tag.getType() == TagType.CATEGORY);
        boolean hasReadingType = tags.stream().anyMatch(tag -> tag.getType() == TagType.READING_TYPE);

        if (!hasGrade) {
            throw new IllegalArgumentException("Книга должна иметь тег класса (10 или 11 класс)");
        }

        if (!hasLevel) {
            throw new IllegalArgumentException("Книга должна иметь тег уровня (База или Профиль)");
        }

        if (!hasCategory) {
            throw new IllegalArgumentException("Книга должна иметь тег категории (Русская или Иностранная литература)");
        }

        // READING_TYPE не обязателен, но если есть, проверяем что только один
        if (hasReadingType) {
            long readingTypeCount = tags.stream()
                    .filter(tag -> tag.getType() == TagType.READING_TYPE)
                    .count();
            if (readingTypeCount > 1) {
                throw new IllegalArgumentException("Книга может иметь только один тег типа чтения");
            }
        }

        // Проверяем, что для GRADE только один тег
        long gradeCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.GRADE)
                .count();
        if (gradeCount > 1) {
            throw new IllegalArgumentException("Книга может быть только для одного класса");
        }

        // Проверяем, что для LEVEL только один тег
        long levelCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.LEVEL)
                .count();
        if (levelCount > 1) {
            throw new IllegalArgumentException("Книга может быть только для одного уровня");
        }

        // Проверяем, что для CATEGORY только один тег
        long categoryCount = tags.stream()
                .filter(tag -> tag.getType() == TagType.CATEGORY)
                .count();
        if (categoryCount > 1) {
            throw new IllegalArgumentException("Книга может быть только для одной категории");
        }
    }
}