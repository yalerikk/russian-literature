package com.literature.russian_literature.userbooks.db;

import com.literature.russian_literature.userbooks.domain.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
    Page<UserBookEntity> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);
    Page<UserBookEntity> findByUserIdAndStatus(Long userId, BookStatus status, Pageable pageable);
    Optional<UserBookEntity> findByUserIdAndBookId(Long userId, Long bookId);
}
