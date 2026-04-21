package com.literature.russian_literature.userbooks.db;

import com.literature.russian_literature.userbooks.domain.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
    Optional<UserBookEntity> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, BookStatus status);
    Page<UserBookEntity> findByUserIdAndStatus(Long userId, BookStatus status, Pageable pageable);
    List<UserBookEntity> findByUserIdAndBookId(Long userId, Long bookId);
    List<UserBookEntity> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, BookStatus status, Pageable pageable);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, BookStatus status);
}
