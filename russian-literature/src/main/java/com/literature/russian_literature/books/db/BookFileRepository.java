package com.literature.russian_literature.books.db;

import com.literature.russian_literature.books.domain.BookFormat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookFileRepository extends JpaRepository<BookFileEntity, Long> {
    List<BookFileEntity> findByBookId(Long bookId);
    Optional<BookFileEntity> findByBookIdAndFormat(Long bookId, BookFormat format);
    boolean existsByBookIdAndFormat(Long bookId, BookFormat format);
}
