package com.literature.russian_literature.userbooks.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.userbooks.db.UserBookEntity;
import com.literature.russian_literature.userbooks.db.UserBookMapper;
import com.literature.russian_literature.userbooks.db.UserBookRepository;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.db.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserBookService {
    private final UserBookRepository repository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookMapper mapper;

    public UserBookService(UserBookRepository repository, UserRepository userRepository,
                           BookRepository bookRepository, UserBookMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    // Получить книги пользователя по статусу (и слайдер, и для страницы коллекции)
    public Page<UserBook> getUserBooksByStatus(Long userId, BookStatus status, Pageable pageable) {
        return repository.findByUserIdAndStatus(userId, status, pageable)
                .map(mapper::toDomain);
    }


    public Page<UserBook> getFavoriteBooks(Long userId, Pageable pageable) {
        return repository.findByUserIdAndIsFavoriteTrue(userId, pageable)
                .map(mapper::toDomain);
    }

    public boolean isBookInFavorite(Long userId, Long bookId) {
        return repository.findByUserIdAndBookId(userId, bookId)
                .map(UserBookEntity::isFavorite)
                .orElse(false);
    }

    public BookStatus getBookProgressStatus(Long userId, Long bookId) {
        return repository.findByUserIdAndBookId(userId, bookId)
                .map(UserBookEntity::getStatus)
                .orElse(null);
    }

    // Добавить или обновить статус книги для пользователя
    @Transactional
    public UserBook addOrUpdateBookStatus(Long userId, Long bookId, BookStatus status, Boolean favorite) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        UserBookEntity entry = repository.findByUserIdAndBookId(userId, bookId)
                .orElse(new UserBookEntity());

        boolean isNew = entry.getId() == null;
        if (isNew) {
            entry.setUser(user);
            entry.setBook(book);
            entry.setCreatedAt(LocalDateTime.now());
        }
        if (favorite != null) {
            entry.setFavorite(favorite);
        }
        if (status != null) {
            entry.setStatus(status);
            switch (status) {
                case READING:
                    entry.setProgress(0);
                    break;
                case READ:
                    entry.setProgress(100);
                    break;
                case WISHLIST:
                    entry.setProgress(null);
                    break;
                default:
                    break;
            }
        }

        entry.setUpdatedAt(LocalDateTime.now());
        if (entry.getStatus() == null && !entry.isFavorite()) {
            if (!isNew) {
                repository.delete(entry);
            }
            return null;
        }

        UserBookEntity saved = repository.save(entry);
        return mapper.toDomain(saved);
    }

    // Обновить прогресс (для READING)
    @Transactional
    public void updateProgress(Long userId, Long bookId, Integer progress) {
        UserBookEntity entry = repository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена в коллекциях пользователя"));

        if (entry.getStatus() != BookStatus.READING) {
            throw new IllegalStateException("Прогресс можно обновлять только для книг в статусе READING");
        }

        entry.setProgress(progress);
        if (progress >= 100) {
            entry.setStatus(BookStatus.READ);
            entry.setProgress(100);
        }
        entry.setUpdatedAt(LocalDateTime.now());
        repository.save(entry);
    }

    // Удалить книгу из коллекции
    @Transactional
    public void removeFromCollection(Long userId, Long bookId, BookStatus status, Boolean favorite) {
        UserBookEntity entry = repository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена в коллекциях пользователя"));

        if (status != null && entry.getStatus() == status) {
            entry.setStatus(null);
            entry.setProgress(null);
        }
        if (Boolean.TRUE.equals(favorite)) {
            entry.setFavorite(false);
        }

        if (entry.getStatus() == null && !entry.isFavorite()) {
            repository.delete(entry);
        } else {
            entry.setUpdatedAt(LocalDateTime.now());
            repository.save(entry);
        }
    }
}
