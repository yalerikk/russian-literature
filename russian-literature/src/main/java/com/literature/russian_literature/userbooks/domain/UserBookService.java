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
import java.util.List;
import java.util.stream.Collectors;

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

    // Получить книги пользователя по статусу (пагинация)
    public Page<UserBook> getUserBooksByStatus(Long userId, BookStatus status, Pageable pageable) {
        return repository.findByUserIdAndStatus(userId, status, pageable)
                .map(mapper::toDomain);
    }

    // Получить первые N книг по статусу (для слайдера)
    public List<UserBook> getRecentUserBooksByStatus(Long userId, BookStatus status, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return repository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    // Добавить или обновить статус книги для пользователя
    @Transactional
    public UserBook addOrUpdateBookStatus(Long userId, Long bookId, BookStatus status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена"));

        // Ищем существующую запись с таким же статусом
        UserBookEntity existing = repository.findByUserIdAndBookIdAndStatus(userId, bookId, status).orElse(null);
        if (existing != null) {
            // Если уже есть – можно обновить прогресс (но для простоты вернём существующую)
            return mapper.toDomain(existing);
        }

        // Если запись с другим статусом существует, её не трогаем. Создаём новую.
        UserBookEntity newEntry = new UserBookEntity();
        newEntry.setUser(user);
        newEntry.setBook(book);
        newEntry.setStatus(status);
        newEntry.setCreatedAt(LocalDateTime.now());
        newEntry.setUpdatedAt(LocalDateTime.now());

        // Прогресс: только для READING и READ
        if (status == BookStatus.READING) {
            newEntry.setProgress(0);
        } else if (status == BookStatus.READ) {
            newEntry.setProgress(100);
        } else {
            newEntry.setProgress(null);
        }

        UserBookEntity saved = repository.save(newEntry);
        return mapper.toDomain(saved);
    }

    // Обновить прогресс (для READING)
    @Transactional
    public void updateProgress(Long userId, Long bookId, Integer progress) {
        UserBookEntity entry = repository.findByUserIdAndBookIdAndStatus(userId, bookId, BookStatus.READING)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена в коллекции в процессе чтения"));
        entry.setProgress(progress);
        if (progress >= 100) {
            // Автоматически переводим в READ
            entry.setStatus(BookStatus.READ);
            entry.setProgress(100);
        }
        entry.setUpdatedAt(LocalDateTime.now());
        repository.save(entry);
    }

    // Сменить статус книги (удаляем старую запись, создаём новую)
    @Transactional
    public UserBook changeBookStatus(Long userId, Long bookId, BookStatus newStatus) {
        UserBookEntity existing = repository.findByUserIdAndBookIdAndStatus(userId, bookId, newStatus).orElse(null);
        if (existing != null) {
            // Уже есть с таким статусом – ничего не делаем
            return mapper.toDomain(existing);
        }

        // Ищем запись с любым статусом, который не FAVORITE (если книга уже в какой-то коллекции прогресса)
        // Для простоты: удаляем все записи, кроме FAVORITE, и создаём новую.
        List<UserBookEntity> all = repository.findByUserIdAndBookId(userId, bookId);
        for (UserBookEntity e : all) {
            if (e.getStatus() != BookStatus.FAVORITE) {
                repository.delete(e);
            }
        }

        UserEntity user = userRepository.findById(userId).orElseThrow();
        BookEntity book = bookRepository.findById(bookId).orElseThrow();
        UserBookEntity newEntry = new UserBookEntity();
        newEntry.setUser(user);
        newEntry.setBook(book);
        newEntry.setStatus(newStatus);
        newEntry.setCreatedAt(LocalDateTime.now());
        newEntry.setUpdatedAt(LocalDateTime.now());

        if (newStatus == BookStatus.READING) {
            newEntry.setProgress(0);
        } else if (newStatus == BookStatus.READ) {
            newEntry.setProgress(100);
        } else {
            newEntry.setProgress(null);
        }

        UserBookEntity saved = repository.save(newEntry);
        return mapper.toDomain(saved);
    }

    // Удалить книгу из коллекции
    @Transactional
    public void removeBookFromCollection(Long userId, Long bookId, BookStatus status) {
        UserBookEntity entry = repository.findByUserIdAndBookIdAndStatus(userId, bookId, status)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена в коллекции " + status));
        repository.delete(entry);
    }

    // Проверка, есть ли книга в избранном (для сердечка на карточке)
    public boolean isBookInFavorite(Long userId, Long bookId) {
        return repository.existsByUserIdAndBookIdAndStatus(userId, bookId, BookStatus.FAVORITE);
    }
}
